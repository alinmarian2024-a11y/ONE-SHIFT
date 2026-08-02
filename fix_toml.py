import re

with open('gradle/libs.versions.toml', 'r') as f:
    content = f.read()

# Remove the previously appended lines if they exist
content = re.sub(r'playServicesAds = "23\.6\.0"\nump = "3\.1\.0"\n\n\[libraries\.play-services-ads\]\ngroup = "com\.google\.android\.gms"\nname = "play-services-ads"\nversion\.ref = "playServicesAds"\n\n\[libraries\.ump\]\ngroup = "com\.google\.android\.ump"\nname = "user-messaging-platform"\nversion\.ref = "ump"', '', content)

if 'playServicesAds =' not in content:
    content = content.replace('[libraries]', 'playServicesAds = "23.6.0"\nump = "3.1.0"\n\n[libraries]')
    content += '\nplay-services-ads = { group = "com.google.android.gms", name = "play-services-ads", version.ref = "playServicesAds" }\n'
    content += 'ump = { group = "com.google.android.ump", name = "user-messaging-platform", version.ref = "ump" }\n'

with open('gradle/libs.versions.toml', 'w') as f:
    f.write(content.strip() + '\n')
