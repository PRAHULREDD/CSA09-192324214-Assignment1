
$ErrorActionPreference = "Stop"

Write-Host "`n===================================="
Write-Host " CSA09 Assignment 1 - Java Tasks"
Write-Host "====================================`n"

# Check Java installation
if (-not (Get-Command javac -ErrorAction SilentlyContinue)) {
    Write-Host "ERROR: javac not found. Install a JDK and configure PATH."
    exit 1
}

java -version
if ($LASTEXITCODE -ne 0) { exit 1 }

$root = $PSScriptRoot
$build = Join-Path $root "build"
New-Item -ItemType Directory -Force -Path $build | Out-Null

# Each task's source folder and main class
$tasks = @(
    @{ Name="Task01"; Dir="Task01"; Main="Task01ProblemModel" },
    @{ Name="Task02"; Dir="Task02"; Main="Task02DataRepresentation" },
    @{ Name="Task03"; Dir="Task03"; Main="ParkingFacility" },
    @{ Name="Task04"; Dir="Task04"; Main="Task04Constructors" },
    @{ Name="Task05"; Dir="Task05"; Main="Task05StaticLifecycle" },
    @{ Name="Task06"; Dir="Task06"; Main="Task06Inheritance" },
    @{ Name="Task07"; Dir="Task07"; Main="Task07Override" },
    @{ Name="Task08"; Dir="Task08"; Main="FeeDemo" },
    @{ Name="Task09"; Dir="Task09"; Main="Task09Abstraction" },
    @{ Name="Task10"; Dir="Task10/src"; Main="parking.app.Main" },
    @{ Name="Task11"; Dir="Task11"; Main="ParkingSystem" },
    @{ Name="Task12"; Dir="Task12"; Main="ParkingSystemV2" }
)

$failed = @()

foreach ($task in $tasks) {
    $name = $task.Name
    $sourceDir = Join-Path $root $task.Dir
    $outputDir = Join-Path $build $name

    Write-Host "`n------------------------------------"
    Write-Host " $name : Compiling"
    Write-Host "------------------------------------"

    New-Item -ItemType Directory -Force -Path $outputDir | Out-Null

    $sources = @(Get-ChildItem -Path $sourceDir -Filter "*.java" -Recurse |
        ForEach-Object { $_.FullName })

    if ($sources.Count -eq 0) {
        Write-Host "ERROR: No Java files found in $sourceDir"
        $failed += $name
        continue
    }

    & javac -encoding UTF-8 -d $outputDir @sources

    if ($LASTEXITCODE -ne 0) {
        Write-Host "COMPILE FAILED: $name"
        $failed += $name
        continue
    }

    Write-Host "`n $name : Running"
    Write-Host "------------------------------------"

    & java -cp $outputDir $task.Main

    if ($LASTEXITCODE -ne 0) {
        Write-Host "EXECUTION FAILED: $name"
        $failed += $name
    } else {
        Write-Host "`n $name : Finished"
    }
}

Write-Host "`n===================================="
Write-Host " FINAL SUMMARY"
Write-Host "===================================="

if ($failed.Count -eq 0) {
    Write-Host "All 12 tasks compiled and executed successfully."
} else {
    Write-Host "Tasks needing attention:"
    $failed | ForEach-Object { Write-Host " - $_" }
}

Write-Host "`nCompiled classes are stored in: $build"
