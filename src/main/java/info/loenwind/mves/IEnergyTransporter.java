package info.loenwind.mves;

/**
 * Energy transporter is a block side capability that can be used by other
 * blocks to detect the ability of a block to transport energy. It must be
 * exposed on all sides of a block that will enact energy transporter
 * functionality and have tile entities. Blocks that do not have tile entities
 * may be part of an energy network. However, it is strongly recommended to have
 * tile entities on all connector blocks.
 * 
 * <p>
 * This capability is not available for items. Any other item and the containing
 * inventory may use the energy supplier and acceptor capabilities of an item at
 * any time. Charging stations may relay an incoming offer to the items they are
 * charging or they charge from their buffer. Same goes for discharging
 * stations.
 * 
 * <p>
 * This capability has no methods; it is a pure marker interface. See
 * IEnergyTransporterRelay.
 * 
 * <h3>Transporting Energy</h3>
 * <p>
 * The transportation of energy is a multi-step process:
 * 
 * <p>
 * Step 1: All connected energy suppliers are asked for their energy stacks.
 * 
 * <p>
 * Step 2: Those stacks are combined into an energy offer.
 * 
 * <p>
 * Step 3: The energy offer is offered to all connected energy acceptors.
 * 
 * <p>
 * Step 4: The energy transporter adds an identification object to the "seen"
 * list of the energy offer, then it is offered to all connected energy
 * transporter relays.
 * 
 * <p>
 * Both step 3 and 4 may be aborted early if the available energy is exhausted.
 * 
 * <p>
 * This process should be repeated once per tick for tile entities and items in
 * (dis-)charging stations or once per second for inventory items. If there was
 * no energy transferred, the transporter may become dormant for up to 10
 * seconds.
 * 
 * <h3>Connecting Energy Transporters</h3>
 * 
 * <p>
 * Energy transportation networks from different mods should be able to link up.
 * The step 4 above achieves this. To prevent backflow and multi-connection
 * problems, any energy transporter that receives an offer must check if the
 * offer already has "seen" its identification object. If it has, the offer must
 * be ignored.
 * 
 * <p>
 * Any energy transporter may opt out of being able to link up with other mod's
 * energy networks by: (a) not implementing IEnergyAcceptor(Relay) interface and
 * (b) not executing step 4 and (c) explaining the user why "it does not work".
 */
public interface IEnergyTransporter extends IMvesCapability {

}
