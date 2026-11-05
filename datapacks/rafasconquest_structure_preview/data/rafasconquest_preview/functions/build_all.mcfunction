# Builds every structure file the mod ships, in a row along +x from where you stand,
# their floors 1 block above your feet (1 block in the air), 6 blocks apart.
# Run it: /function rafasconquest_preview:build_all
place template rafasconquest:city/wall/corner ~ ~1 ~
place template rafasconquest:city/wall/gate ~40 ~1 ~
place template rafasconquest:city/wall/segment ~78 ~1 ~
tellraw @s [{"text":"Built along +x: ","color":"gold"},{"text":"city/wall/corner (34x8x5) at +0, city/wall/gate (32x8x5) at +40, city/wall/segment (32x8x5) at +78","color":"white"}]
