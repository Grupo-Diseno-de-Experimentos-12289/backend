# Insert /** ... */ Javadoc stubs for every MissingJavadocType / MissingJavadocMethod
# violation reported by Checkstyle. Reads target/checkstyle-result.xml.

$ErrorActionPreference = 'Stop'
$reportPath = 'target/checkstyle-result.xml'

[xml]$report = Get-Content -Raw -Path $reportPath

$targets = @{}
foreach ($file in $report.checkstyle.file) {
    $path = $file.name
    $lines = New-Object System.Collections.Generic.HashSet[int]
    foreach ($err in @($file.error)) {
        if ($null -eq $err) { continue }
        $src = $err.source
        if ($src -match 'MissingJavadocType' -or $src -match 'MissingJavadocMethod') {
            $null = $lines.Add([int]$err.line)
        }
    }
    if ($lines.Count -gt 0) {
        $targets[$path] = $lines
    }
}

function Get-Stub {
    param([string]$Indent, [string]$DeclLine, [string]$ClassName)

    if ($DeclLine -match '^\s*(public|protected|private|abstract|final|static|\s)*\s*(class|interface|record|enum|@interface)\s+([A-Za-z_][A-Za-z0-9_]*)') {
        $kind = $matches[2]
        $name = $matches[3]
        switch ($kind) {
            'interface' { return "$Indent/** $name contract. */" }
            'enum'      { return "$Indent/** $name enumeration. */" }
            'record'    { return "$Indent/** $name value carrier. */" }
            default     { return "$Indent/** $name type. */" }
        }
    }

    if ($DeclLine -match "^\s*(public|protected|private)?\s*$([regex]::Escape($ClassName))\s*\(") {
        return "$Indent/** Constructs a new $ClassName. */"
    }

    if ($DeclLine -match '^\s*(?:@\w+(?:\([^)]*\))?\s*)*(?:public|protected|private|abstract|default|static|final|synchronized|native|\s)*\s*(?:<[^>]+>\s*)?[\w<>\[\],\s\.\?]+\s+([A-Za-z_][A-Za-z0-9_]*)\s*\(') {
        $method = $matches[1]
        $words = [regex]::Replace($method, '(?<!^)(?=[A-Z])', ' ').ToLower()
        $cap = $words.Substring(0,1).ToUpper() + $words.Substring(1)
        return "$Indent/** $cap. */"
    }

    return "$Indent/** Documentation. */"
}

$totalInserted = 0
foreach ($entry in $targets.GetEnumerator()) {
    $path = $entry.Key
    $lineSet = $entry.Value
    $className = [System.IO.Path]::GetFileNameWithoutExtension($path)

    $content = [System.IO.File]::ReadAllLines($path)
    $list = New-Object System.Collections.Generic.List[string]
    foreach ($l in $content) { $list.Add($l) }

    $sortedLines = $lineSet | Sort-Object -Descending
    foreach ($ln in $sortedLines) {
        $idx = $ln - 1
        if ($idx -lt 0 -or $idx -ge $list.Count) { continue }

        $scan = $idx
        while ($scan -lt $list.Count -and $list[$scan].TrimStart().StartsWith('@')) {
            $scan++
        }
        $declLine = if ($scan -lt $list.Count) { $list[$scan] } else { $list[$idx] }

        if ($list[$idx] -match '^(\s*)') { $indent = $matches[1] } else { $indent = '' }

        $prevIdx = $idx - 1
        while ($prevIdx -ge 0 -and $list[$prevIdx].TrimStart().StartsWith('@')) { $prevIdx-- }
        if ($prevIdx -ge 0 -and $list[$prevIdx].TrimEnd().EndsWith('*/')) { continue }

        $stub = Get-Stub -Indent $indent -DeclLine $declLine -ClassName $className
        $list.Insert($idx, $stub)
        $totalInserted++
    }

    [System.IO.File]::WriteAllLines($path, $list.ToArray(), [System.Text.UTF8Encoding]::new($false))
}

"Inserted $totalInserted Javadoc stubs across $($targets.Count) files"
