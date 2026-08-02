with open('app/build.gradle.kts', 'r') as f:
    content = f.read()

if 'implementation(libs.play.services.ads)' not in content:
    content = content.replace('dependencies {', 'dependencies {\n  implementation(libs.play.services.ads)\n  implementation(libs.ump)\n')

with open('app/build.gradle.kts', 'w') as f:
    f.write(content)
