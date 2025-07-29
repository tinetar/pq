package dre.elfocrash.roboto.ai.walker;

import dre.elfocrash.roboto.FFFFakePlayer;
import dre.elfocrash.roboto.model.WWWWalkNode;
import dre.elfocrash.roboto.model.WWWWalkerType;

import net.sf.l2j.commons.random.Rnd;

public class GGGGiranWalkerAI extends WWWWalkerAI {
	
	public GGGGiranWalkerAI(FFFFakePlayer character) {
		super(character);
	}
	
	@Override
	protected WWWWalkerType getWalkerType() {
		return WWWWalkerType.LINEAR;
	}
	
	@Override
	protected void setWalkNodes() {
		_walkNodes.add(new WWWWalkNode(83539, 146609, -16360, Rnd.get(2, 2))); //otan girnai prin stamatisi
		_walkNodes.add(new WWWWalkNode(83489, 146581, -16360, Rnd.get(1200, 1200))); //pano apo trapeza
		_walkNodes.add(new WWWWalkNode(83805, 147809, -16360, Rnd.get(3, 3))); //
		_walkNodes.add(new WWWWalkNode(83479, 147967, -16360, Rnd.get(3, 3))); //
		
		_walkNodes.add(new WWWWalkNode(83478, 147966, -3408, Rnd.get(20, 30))); //teleport proti 
		_walkNodes.add(new WWWWalkNode(83479, 147967, -3408, Rnd.get(20, 30))); //teleport proti 
		
		_walkNodes.add(new WWWWalkNode(82345, 148783, -3470, Rnd.get(40, 60))); //
		_walkNodes.add(new WWWWalkNode(81959, 149084, -3470, Rnd.get(40, 60))); //
		_walkNodes.add(new WWWWalkNode(83204, 148479, -3425, Rnd.get(40, 60))); //
		_walkNodes.add(new WWWWalkNode(83809, 148604, -3396, Rnd.get(20, 30))); //aliteleport 
		_walkNodes.add(new WWWWalkNode(83810, 148605, -2234, Rnd.get(0, 0))); //pano
		_walkNodes.add(new WWWWalkNode(84346, 148445, -2234, Rnd.get(3, 3))); //pano ksekinai to alo apo kato
		
		_walkNodes.add(new WWWWalkNode(83539, 146609, -16360, Rnd.get(2, 2))); //otan girnai prin stamatisi
		_walkNodes.add(new WWWWalkNode(83489, 146581, -16360, Rnd.get(1200, 1200))); //pano apo trapeza
		_walkNodes.add(new WWWWalkNode(83805, 147809, -16360, Rnd.get(3, 3))); //
		_walkNodes.add(new WWWWalkNode(83479, 147967, -16360, Rnd.get(3, 3))); //
		
		_walkNodes.add(new WWWWalkNode(83478, 147966, -3408, Rnd.get(20, 30))); //teleport proti 
		_walkNodes.add(new WWWWalkNode(83479, 147967, -3408, Rnd.get(20, 30))); //teleport proti 
		
		_walkNodes.add(new WWWWalkNode(82329, 148700, -3470, Rnd.get(40, 60))); //
		_walkNodes.add(new WWWWalkNode(82064, 148273, -3470, Rnd.get(0, 0))); //
		_walkNodes.add(new WWWWalkNode(81963, 148329, -3470, Rnd.get(40, 60))); //
		_walkNodes.add(new WWWWalkNode(83086, 148936, -3472, Rnd.get(40, 60))); //
		_walkNodes.add(new WWWWalkNode(83902, 149029, -3405, Rnd.get(20, 30))); //teleport
		_walkNodes.add(new WWWWalkNode(83901, 149028, -2234, Rnd.get(0, 0))); //pano
		_walkNodes.add(new WWWWalkNode(84346, 148445, -2234, Rnd.get(3, 3))); //pano ksekinai to alo apo kato 
		
		_walkNodes.add(new WWWWalkNode(83539, 146609, -16360, Rnd.get(2, 2))); //otan girnai prin stamatisi
		_walkNodes.add(new WWWWalkNode(83489, 146581, -16360, Rnd.get(1200, 1200))); //pano apo trapeza
		_walkNodes.add(new WWWWalkNode(83805, 147809, -16360, Rnd.get(3, 3))); //
		_walkNodes.add(new WWWWalkNode(83479, 147967, -16360, Rnd.get(3, 3))); //
		
		_walkNodes.add(new WWWWalkNode(83478, 147966, -3408, Rnd.get(20, 30))); //teleport
		_walkNodes.add(new WWWWalkNode(83479, 147967, -3408, Rnd.get(20, 30))); //teleport proti 
		
		_walkNodes.add(new WWWWalkNode(81971, 148190, -3470, Rnd.get(40, 60))); //
		_walkNodes.add(new WWWWalkNode(82207, 148436, -3470, Rnd.get(40, 60))); //
		_walkNodes.add(new WWWWalkNode(83060, 148397, -3472, Rnd.get(40, 60))); //
		_walkNodes.add(new WWWWalkNode(83803, 148151, -3396, Rnd.get(20, 30))); //teleport
		_walkNodes.add(new WWWWalkNode(83804, 148152, -2234, Rnd.get(0, 0))); //pano
		_walkNodes.add(new WWWWalkNode(84346, 148445, -2234, Rnd.get(3, 3))); //pano ksekinai to alo apo kato
		
		_walkNodes.add(new WWWWalkNode(83539, 146609, -16360, Rnd.get(2, 2))); //otan girnai prin stamatisi
		_walkNodes.add(new WWWWalkNode(83489, 146581, -16360, Rnd.get(1200, 1200))); //pano apo trapeza
		_walkNodes.add(new WWWWalkNode(83805, 147809, -16360, Rnd.get(3, 3))); //
		_walkNodes.add(new WWWWalkNode(83479, 147967, -16360, Rnd.get(3, 3))); //
		
		_walkNodes.add(new WWWWalkNode(83478, 147966, -3408, Rnd.get(20, 30))); //teleport proti 
		_walkNodes.add(new WWWWalkNode(83479, 147967, -3408, Rnd.get(20, 30))); //teleport proti 
		
		_walkNodes.add(new WWWWalkNode(82295, 148388, -3470, Rnd.get(40, 60)));
		_walkNodes.add(new WWWWalkNode(82356, 148596, -3470, Rnd.get(40, 60)));
		_walkNodes.add(new WWWWalkNode(83086, 148787, -3472, Rnd.get(40, 60)));
		_walkNodes.add(new WWWWalkNode(83805, 148570, -3396, Rnd.get(20, 30))); //aliteleport 
		_walkNodes.add(new WWWWalkNode(83803, 148566, -2234, Rnd.get(0, 0))); //pano
		_walkNodes.add(new WWWWalkNode(84346, 148445, -2234, Rnd.get(3, 3))); //pano ksekinai to alo apo kato
		
		_walkNodes.add(new WWWWalkNode(83539, 146609, -16360, Rnd.get(2, 2))); //otan girnai prin stamatisi
		_walkNodes.add(new WWWWalkNode(83489, 146581, -16360, Rnd.get(1200, 1200))); //pano apo trapeza
		_walkNodes.add(new WWWWalkNode(83805, 147809, -16360, Rnd.get(3, 3))); //
		_walkNodes.add(new WWWWalkNode(83479, 147967, -16360, Rnd.get(3, 3))); //
		
		_walkNodes.add(new WWWWalkNode(83478, 147966, -3408, Rnd.get(20, 30))); //teleport proti 
		_walkNodes.add(new WWWWalkNode(83479, 147967, -3408, Rnd.get(20, 30))); //teleport proti 
		
		_walkNodes.add(new WWWWalkNode(82372, 148652, -3470, Rnd.get(40, 60))); //
		_walkNodes.add(new WWWWalkNode(82897, 149348, -3472, Rnd.get(40, 60))); //
		_walkNodes.add(new WWWWalkNode(83049, 148876, -3472, Rnd.get(40, 60))); //
		_walkNodes.add(new WWWWalkNode(82661, 147738, -3472, Rnd.get(40, 60))); //
		_walkNodes.add(new WWWWalkNode(83855, 148667, -3396, Rnd.get(20, 30))); //aliteleport 
		_walkNodes.add(new WWWWalkNode(83856, 148668, -2234, Rnd.get(0, 0))); //pano
		_walkNodes.add(new WWWWalkNode(84346, 148445, -2234, Rnd.get(3, 3))); //pano ksekinai to alo apo kato
		
		_walkNodes.add(new WWWWalkNode(83539, 146609, -16360, Rnd.get(2, 2))); //otan girnai prin stamatisi
		_walkNodes.add(new WWWWalkNode(83489, 146581, -16360, Rnd.get(1200, 1200))); //pano apo trapeza
		_walkNodes.add(new WWWWalkNode(83805, 147809, -16360, Rnd.get(3, 3))); //
		_walkNodes.add(new WWWWalkNode(83479, 147967, -16360, Rnd.get(3, 3))); //
		
		_walkNodes.add(new WWWWalkNode(83478, 147966, -3408, Rnd.get(20, 30))); //teleport proti 
		_walkNodes.add(new WWWWalkNode(83479, 147967, -3408, Rnd.get(20, 30))); //teleport proti 
		
		_walkNodes.add(new WWWWalkNode(81971, 148256, -3470, Rnd.get(40, 60))); //
		_walkNodes.add(new WWWWalkNode(82257, 148451, -3470, Rnd.get(40, 60))); //
		_walkNodes.add(new WWWWalkNode(82286, 148686, -3470, Rnd.get(40, 60))); //
		_walkNodes.add(new WWWWalkNode(83186, 148793, -3439, Rnd.get(40, 60))); //
		_walkNodes.add(new WWWWalkNode(83897, 148603, -3405, Rnd.get(20, 30))); //teleport
		_walkNodes.add(new WWWWalkNode(83898, 148604, -2234, Rnd.get(0, 0))); //pano
		_walkNodes.add(new WWWWalkNode(84346, 148445, -2234, Rnd.get(3, 3))); //pano ///ksekinai to alo apo kato 
		
		_walkNodes.add(new WWWWalkNode(83539, 146609, -16360, Rnd.get(2, 2))); //otan girnai prin stamatisi
		_walkNodes.add(new WWWWalkNode(83489, 146581, -16360, Rnd.get(1200, 1200))); //pano apo trapeza
		_walkNodes.add(new WWWWalkNode(83805, 147809, -16360, Rnd.get(3, 3))); //
		_walkNodes.add(new WWWWalkNode(83479, 147967, -16360, Rnd.get(3, 3))); //
		
		_walkNodes.add(new WWWWalkNode(83478, 147966, -3408, Rnd.get(20, 30))); //teleport proti 
		_walkNodes.add(new WWWWalkNode(83479, 147967, -3408, Rnd.get(20, 30))); //teleport proti 
		
		_walkNodes.add(new WWWWalkNode(82320, 148875, -3470, Rnd.get(40, 60))); //
		_walkNodes.add(new WWWWalkNode(81985, 148995, -3470, Rnd.get(40, 60))); //
		_walkNodes.add(new WWWWalkNode(82778, 149301, -3472, Rnd.get(40, 60))); //
		_walkNodes.add(new WWWWalkNode(83840, 149104, -3396, Rnd.get(20, 30))); //teleport
		_walkNodes.add(new WWWWalkNode(83841, 149105, -2234, Rnd.get(0, 0))); //pano
		_walkNodes.add(new WWWWalkNode(84346, 148445, -2234, Rnd.get(3, 3))); //pano ksekinai to alo apo kato
		
		_walkNodes.add(new WWWWalkNode(83539, 146609, -16360, Rnd.get(2, 2))); //otan girnai prin stamatisi
		_walkNodes.add(new WWWWalkNode(83489, 146581, -16360, Rnd.get(1200, 1200))); //pano apo trapeza
		_walkNodes.add(new WWWWalkNode(83805, 147809, -16360, Rnd.get(3, 3))); //
		_walkNodes.add(new WWWWalkNode(83479, 147967, -16360, Rnd.get(3, 3))); //
		
		_walkNodes.add(new WWWWalkNode(83478, 147966, -3408, Rnd.get(20, 30))); //teleport
		_walkNodes.add(new WWWWalkNode(83479, 147967, -3408, Rnd.get(20, 30))); //teleport proti 
		
		_walkNodes.add(new WWWWalkNode(82346, 148485, -3467, Rnd.get(40, 60)));
		_walkNodes.add(new WWWWalkNode(82257, 148628, -3470, Rnd.get(40, 60)));
		_walkNodes.add(new WWWWalkNode(83036, 148469, -3472, Rnd.get(40, 60)));
		_walkNodes.add(new WWWWalkNode(83820, 149022, -3397, Rnd.get(20, 30))); //aliteleport
		_walkNodes.add(new WWWWalkNode(83819, 149023, -2234, Rnd.get(0, 0))); //pano
		_walkNodes.add(new WWWWalkNode(84346, 148445, -2234, Rnd.get(3, 3))); //pano ksekinai to alo apo kato
		
		_walkNodes.add(new WWWWalkNode(83539, 146609, -16360, Rnd.get(2, 2))); //otan girnai prin stamatisi
		_walkNodes.add(new WWWWalkNode(83489, 146581, -16360, Rnd.get(1200, 1200))); //pano apo trapeza
		_walkNodes.add(new WWWWalkNode(83805, 147809, -16360, Rnd.get(3, 3))); //
		_walkNodes.add(new WWWWalkNode(83479, 147967, -16360, Rnd.get(3, 3))); //
		
		_walkNodes.add(new WWWWalkNode(83478, 147966, -3408, Rnd.get(20, 30))); //teleport proti 
		_walkNodes.add(new WWWWalkNode(83479, 147967, -3408, Rnd.get(20, 30))); //teleport proti 
		
		_walkNodes.add(new WWWWalkNode(82345, 148783, -3470, Rnd.get(40, 60))); //
		_walkNodes.add(new WWWWalkNode(81959, 149084, -3470, Rnd.get(40, 60))); //
		_walkNodes.add(new WWWWalkNode(83204, 148479, -3425, Rnd.get(40, 60))); //
		_walkNodes.add(new WWWWalkNode(83809, 148604, -3396, Rnd.get(20, 30))); //aliteleport 
		_walkNodes.add(new WWWWalkNode(83810, 148605, -2234, Rnd.get(0, 0))); //pano
		_walkNodes.add(new WWWWalkNode(84346, 148445, -2234, Rnd.get(3, 3))); //pano ksekinai to alo apo kato
		
		_walkNodes.add(new WWWWalkNode(83539, 146609, -16360, Rnd.get(2, 2))); //otan girnai prin stamatisi
		_walkNodes.add(new WWWWalkNode(83489, 146581, -16360, Rnd.get(1200, 1200))); //pano apo trapeza
		_walkNodes.add(new WWWWalkNode(83805, 147809, -16360, Rnd.get(3, 3))); //
		_walkNodes.add(new WWWWalkNode(83479, 147967, -16360, Rnd.get(3, 3))); //
		
		_walkNodes.add(new WWWWalkNode(83478, 147966, -3408, Rnd.get(20, 30))); //teleport proti 
		_walkNodes.add(new WWWWalkNode(83479, 147967, -3408, Rnd.get(20, 30))); //teleport proti 
		
		_walkNodes.add(new WWWWalkNode(82320, 148875, -3470, Rnd.get(40, 60))); //
		_walkNodes.add(new WWWWalkNode(81985, 148995, -3470, Rnd.get(40, 60))); //
		_walkNodes.add(new WWWWalkNode(82778, 149301, -3472, Rnd.get(40, 60))); //
		_walkNodes.add(new WWWWalkNode(83840, 149104, -3396, Rnd.get(20, 30))); //teleport
		_walkNodes.add(new WWWWalkNode(83841, 149105, -2234, Rnd.get(0, 0))); //pano
		_walkNodes.add(new WWWWalkNode(84346, 148445, -2234, Rnd.get(3, 3))); //pano ksekinai to alo apo kato
		
		_walkNodes.add(new WWWWalkNode(83539, 146609, -16360, Rnd.get(2, 2))); //otan girnai prin stamatisi
		_walkNodes.add(new WWWWalkNode(83489, 146581, -16360, Rnd.get(1200, 1200))); //pano apo trapeza
		_walkNodes.add(new WWWWalkNode(83805, 147809, -16360, Rnd.get(3, 3))); //
		_walkNodes.add(new WWWWalkNode(83479, 147967, -16360, Rnd.get(3, 3))); //
		
		_walkNodes.add(new WWWWalkNode(83478, 147966, -3408, Rnd.get(20, 30))); //teleport proti 
		_walkNodes.add(new WWWWalkNode(83479, 147967, -3408, Rnd.get(20, 30))); //teleport proti 
		
		_walkNodes.add(new WWWWalkNode(82295, 148388, -3470, Rnd.get(40, 60)));
		_walkNodes.add(new WWWWalkNode(82356, 148596, -3470, Rnd.get(40, 60)));
		_walkNodes.add(new WWWWalkNode(83086, 148787, -3472, Rnd.get(40, 60)));
		_walkNodes.add(new WWWWalkNode(83805, 148570, -3396, Rnd.get(20, 30))); //aliteleport 
		_walkNodes.add(new WWWWalkNode(83803, 148566, -2234, Rnd.get(0, 0))); //pano
		_walkNodes.add(new WWWWalkNode(84346, 148445, -2234, Rnd.get(3, 3))); //pano ksekinai to alo apo kato
		
		_walkNodes.add(new WWWWalkNode(83539, 146609, -16360, Rnd.get(2, 2))); //otan girnai prin stamatisi
	}
}
