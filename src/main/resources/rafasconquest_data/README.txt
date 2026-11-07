Rafa's Conquest data: quests and dialogue, as .toml files. Copied out to mods/RafasConquest/ when missing.

quests/    One file per kind of quest; subfolders are fine. Add a file to add a quest; set weight = 0 to switch one off.
dialogue/  Everything characters say and the game tells players: talk, the war council, battle orders and reports,
           heralds, feuds and the chronicle. A key is its file and place: greet in noble.toml is noble.greet.
           A line is a text, or a list of texts (one is said at random). {words} are filled in by the game.

The mod's own copies are read first, then yours: a line you leave out falls back to the mod's.
Read on server start, and again with /pve data reload.
