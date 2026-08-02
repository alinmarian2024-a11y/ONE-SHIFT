with open('gradle/libs.versions.toml', 'r') as f:
    lines = f.readlines()

libraries_index = lines.index('[libraries]\n')

# Remove the incorrectly added plugins
new_lines = []
for line in lines:
    if line.startswith('play-services-ads =') or line.startswith('ump = { group'):
        continue
    new_lines.append(line)

# Add them back after [libraries]
libraries_index = new_lines.index('[libraries]\n')
new_lines.insert(libraries_index + 1, 'play-services-ads = { group = "com.google.android.gms", name = "play-services-ads", version.ref = "playServicesAds" }\n')
new_lines.insert(libraries_index + 2, 'ump = { group = "com.google.android.ump", name = "user-messaging-platform", version.ref = "ump" }\n')

with open('gradle/libs.versions.toml', 'w') as f:
    f.writelines(new_lines)
