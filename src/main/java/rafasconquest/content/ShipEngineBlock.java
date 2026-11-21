package rafasconquest.content;

import rafasconquest.battle.Machine;

/**
 * A ship's engine on its own block, as the {@link MediumCannonBlock} is a ship's gun: an onager, trebuchet, arcballista or
 * catapult, drawn with Siegeworks' own model (DistantArmyRenderer.shipEngines), worked and fired as the cannon is
 * ({@link MediumCannonEntity}) but throwing its engine card's shot: no barrel to tip, it only swings left and right, and
 * its crew pick the throw's angle. Not a Siegeworks entity, which a Sable ship can't carry.
 */
public class ShipEngineBlock extends MediumCannonBlock {
    private final String machine;

    public ShipEngineBlock(Properties properties, String machine) {
        super(properties);
        this.machine = machine;
    }

    @Override
    public Machine machine() {
        Machine m = Machine.byId(machine);
        return m != null ? m : Machine.MEDIUM_CANNON;
    }
}
