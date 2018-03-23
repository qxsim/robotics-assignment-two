package rp.assignments.individual.ex2;

import lejos.robotics.RangeFinder;
import lejos.robotics.navigation.DifferentialPilot;
import rp.config.RangeFinderDescription;
import rp.robotics.DifferentialDriveRobot;
import rp.systems.StoppableRunnable;

public class RangeController implements StoppableRunnable {
	
	DifferentialDriveRobot robot;
	RangeFinder ranger;
	RangeFinderDescription desc;
	Boolean run = false;
	Boolean speedUp = false;
	Double robotLength;
	float maxDistance;
	
	public RangeController(DifferentialDriveRobot robot, RangeFinderDescription desc, RangeFinder ranger, float maxDistance) {
		this.robot = robot;
		this.desc = desc;
		this.ranger = ranger;
		this.robotLength = robot.getRobotLength();
		maxDistance = robotLength.floatValue();
	}
	
	public void speedUp() {
		speedUp = true;
	}

	@Override
	public void run() {
		DifferentialPilot DP = this.robot.getDifferentialPilot();
		run = true;
		float infinity = Float.POSITIVE_INFINITY;
		
		DP.travel(infinity, true);
		
		while (run) {
			while (DP.isMoving()) {
				if (this.ranger.getRange() > robotLength) {
					speedUp();
					while (speedUp) {
						DP.setTravelSpeed(DP.getMaxTravelSpeed());
						if (!(this.ranger.getRange() > robotLength)) {
							speedUp = false;
							break;
						}
					}
				}
				if (this.ranger.getRange() < robotLength) {
					DP.stop();
					break;
				}
			}
			DP.travel(infinity, true);
		}
	}
	
	@Override
	public void stop() {
		run = false;
	}
}