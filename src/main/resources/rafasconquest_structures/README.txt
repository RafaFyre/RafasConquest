RafasConquest structures
========================

Drop structure files (.nbt) into these folders. Each folder is a slot; any file in it may be built there.
Files are read as land is generated, so a new file is used for land generated after you add it.
In a running game, /pve style reload re-reads the folders. Places already built stay as they are.

style/<name>/      A building style. Its style.json names the homelands that build in it:
                     { "homelands": ["plains", "desert", "jungle", "savanna", "snow", "swamp", "taiga"] }
                   A slot this style leaves empty uses the default style's; one empty in both is built
                   the way the mod builds it itself.
  city/walls/corners, gates, watchtowers, walls
  city/walls/junctions                               the Wall Tower Junction: an outer wall piece, built like a wall
                                                     segment, set where a noble district against the city's edge has
                                                     its walls meet the outer wall, at its middle (else corner towers)
  city/buildings/common                              ordinary houses
  city/buildings/noble                               noble buildings
  city/buildings/landmarks/mandatory/palace          the Palace: the gold church the city's holder (and capital lord) stand in
  city/buildings/landmarks/mandatory/barracks/small  the small Barracks: the recruitment post, its recruiter, guards and quest board
  city/buildings/landmarks/style/churches, monuments
  city/buildings/landmarks/unique
  lone/castles/small, medium, large, grand           small: a lord's castle; medium: a county nation's seat;
                                                     large: a duchy's; grand: a kingdom's or an empire's
  lone/hamlets                                       hamlets in a nation's land
  ships/warships/small, medium, large, xlarge        warships (folders only: not built yet)
  ships/merchant/small, medium, large, xlarge        merchant ships (folders only: not built yet)

world/<land>/      The wild of each land (plains, desert, jungle, savanna, snow, swamp, taiga):
  ruins, hamlets (in no nation's land), dungeons

Building rules
--------------
- The template's y = 0 is its floor layer, set in place of the ground's top block, centred on the spot. Lone
  structures are turned a random quarter turn; city buildings keep their door to the west (build it facing north).
- City plots are 14 x 14 blocks: the Palace and small Barracks are centred on theirs. Put a Quest Board in the Barracks.
- Where a style's Palace or Barracks stands, the mod places none of its own people there: use Character Markers
  (City holder, and a Named Character of rank capital lord, for the Palace; Recruiter and guards for the Barracks).
- Structure blocks are left out; air in the template clears what was there (use structure void to keep the ground).
- Put Character Markers (blue bedrock, creative tab) where people should stand, facing the way they should face.
  Right-click one to choose who. Markers placed by hand only show a label; markers in generated structures
  become their people once the chunk loads, then turn into air (or the block you choose).

Saving from the game
--------------------
Save with a structure block as rafasconquest:<name>, then run
  /pve style save <style> "<slot>" <name>        e.g. /pve style save default "lone/castles/small" my_keep
to move it into the slot. For the wild, use the slot "world/<land>/<kind>", e.g. "world/plains/ruins".

This folder is never overwritten: the mod only adds files it ships that are missing.
