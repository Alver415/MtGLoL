if ([System.Threading.Thread]::CurrentThread.ApartmentState -ne [System.Threading.ApartmentState]::MTA){ 
    powershell.exe -MTA -File $MyInvocation.MyCommand.Path 
    return 
}

Get-EventSubscriber -Force | Unregister-Event -Force
Add-Type -Assembly System.IO.Compression.FileSystem

$global:currentDirectory = (Get-Item (Get-Location)).FullName
$global:baseDirectory = (Get-Item $currentDirectory).parent.FullName
$global:logFile = "$currentDirectory\MseSetMonitor.log"
$global:logLevels = 
$global:extractAll = $false
$global:clean = $true
$global:runOnStartup = $true

### LOGGING ###
function Write-Log {
    param (
        [string] $message,
        [string] $level,
        [string] $foregroundColor
    )
    if (!$logLevels -or $logLevels -contains $level.Replace(':', '').Trim()) {
        if (-Not $foregroundColor){ $foregroundColor = "White" } #Default to white if empty

        $formatted = "$(Get-Date) $level`t$message";
        Write-Host $formatted -ForegroundColor $foregroundColor
        Add-content $logFile -Value $formatted 
    }
}
    
function Log    { param ([string] $message, [string] $foregroundColor) Write-Log $message "ALL  : "  $foregroundColor }
function Trace  { param ([string] $message)                            Write-Log $message "TRACE: " "DarkGray"        }
function Debug  { param ([string] $message)                            Write-Log $message "DEBUG: " "Gray"            }
function Info   { param ([string] $message)                            Write-Log $message "INFO : " "White"           }
function Warn   { param ([string] $message)                            Write-Log $message "WARN : " "Yellow"          }
function Error  { param ([string] $message)                            Write-Log $message "ERROR: " "Red"             }

### UTILITY ###
function New-TemporaryZipFile {
    $directory = [System.IO.Path]::GetTempPath()
    [string] $name = [System.Guid]::NewGuid()
    "$directory$name.zip"
}
function Remove-Extension { param ([string] $path) [IO.Path]::ChangeExtension($path, [NullString]::Value)}
function Get-Extension { param ([string] $path) [IO.Path]::GetExtension($path) }

### FUNCTIONALITY ###
function Extract {
    param (
        [string] $path
    )
    Log "Extract - $path" "Cyan"
    $file = Get-Item $path
    $directory = $file.Directory.FullName
    $fileBaseName = $file.BaseName -split "\."
    $destination = "$directory\$fileBaseName"

    $destinationExists = Test-Path $destination
    if ($clean -and $destinationExists) {
        Warn "`tCleaning destination directory: $destination"
        Remove-Item $destination -Recurse
        New-Item -Path $destination -ItemType Directory
    } elseif (-Not $destinationExists) {
        Debug "`tCreating destination directory: $destination"
        New-Item -Path $destination -ItemType Directory
    } else {
        Trace "``tUsing destination directory: $destination"
    }
        
    $temp = New-TemporaryZipFile
    Debug "`tCopy to temp file: $temp"
    Copy-Item -Path $path -Destination $temp
                    
    Debug "`tOpen archive"
    $zip = [IO.Compression.ZipFile]::OpenRead($temp)
    $extracted = New-Object System.Collections.ArrayList
    $skipped = New-Object System.Collections.ArrayList
    foreach ($entry in $zip.Entries) {
        $entryName = $entry.Name
        if (($entryName -eq "set") -or ($extractAll)){
            #Info  "`tExtracting: $entryName"
            [System.IO.Compression.ZipFileExtensions]::ExtractToFile($entry, "$destination\$entryName", $true)
            $extracted.Add($entryName) > $null
        } else {
            #Info  "`tSkipping:   $entryName"
            $skipped.Add($entryName) > $null
        }
    }
    Info  "`tExtracted: $($extracted -join ', ')"
    Trace "`tSkipped:   $($skipped   -join ', ')"
        
    Debug "`tClose archive"
    $zip.Dispose()

    Debug "`tDeleting temp file: $temp" 
    Remove-Item $temp
}

function Hide {
    param (
        [string] $path
    )
    Log "Hide Backup - $path" "DarkMagenta"
    Trace "`tHiding: $path"
    (Get-Item $path).attributes='Hidden'
}

function Delete {
    param (
        [string] $path
    )
    Log "Delete - $path" "Magenta"
    $oldDirectory = Remove-Extension $path
    if (Test-Path $oldDirectory){
        Info "`tDeleting: $oldDirectory"
        Remove-Item $oldDirectory -Recurse
    }
}

$action = { 
    try {
        $path = $Event.SourceEventArgs.FullPath
        $changeType = $Event.SourceEventArgs.ChangeType
        
        Trace "Event '$changeType' Triggered for Path: $path"
        $extension = Get-Extension $path
        if ($changeType -eq "Delete"){
            if ($extension -eq ".mse-set"){
                Delete $path
            }
        } elseif ($extension -eq ".bak") {
            Hide $path 
        } elseif ($extension -eq ".mse-set") {
            Extract $path
        }
    } catch {
        Error $_.Exception
    }
}

if ($runOnStartup) {
    Log "Extracting All..." "Green"
    foreach ($path in (Get-ChildItem -Path $baseDirectory -Filter "*.mse-set" -Recurse | %{$_.FullName})){
        Extract $path > $null
    }
}

Log   "Starting Change Monitor..." "Green"     
Debug "baseDirectory:    $baseDirectory"    
Debug "currentDirectory: $currentDirectory" 
Debug "logFile:          $logFile"          

$watcher = New-Object System.IO.FileSystemWatcher
$watcher.Path = $baseDirectory
$watcher.Filter = "*.mse-set"
$watcher.IncludeSubdirectories = $true
$watcher.EnableRaisingEvents = $true  

Register-ObjectEvent $watcher "Created" -Action $action > $null
Register-ObjectEvent $watcher "Changed" -Action $action > $null
Register-ObjectEvent $watcher "Renamed" -Action $action > $null
Register-ObjectEvent $watcher "Deleted" -Action $action > $null

Wait-Event -SourceIdentifier FileWatcher