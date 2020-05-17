import java.util.Random;

import junit.framework.Assert;
//import org.junit.Assert;
import nz.ac.waikato.modeljunit.Action;
import nz.ac.waikato.modeljunit.FsmModel;
import nz.ac.waikato.modeljunit.GreedyTester;
import nz.ac.waikato.modeljunit.Tester;
import nz.ac.waikato.modeljunit.coverage.ActionCoverage;
import nz.ac.waikato.modeljunit.coverage.CoverageMetric;
import nz.ac.waikato.modeljunit.coverage.StateCoverage;
import nz.ac.waikato.modeljunit.coverage.TransitionCoverage;
import nz.ac.waikato.modeljunit.coverage.TransitionPairCoverage;
import theft_alarm_v5.Alarm;

public class AlarmSystemTesting implements FsmModel {
	//private Random rand = new Random();
	private Alarm ac = new Alarm();
	private State state; // enum defined below
	private boolean arm_pressed_and_doors_are_open;
	private boolean arm_pressed_and_doors_are_closed;
	private boolean disarm;
	private boolean siren_ends_and_doors_are_closed;
	private boolean door_opens;

	private enum State {
		DISARMED, ARMED, SIREN;
	}

	@Override
	public Object getState() {
		return (String.valueOf(state));
	}

	@Override
	public void reset(boolean testing) {
		state = State.DISARMED;
		arm_pressed_and_doors_are_open = false;
		arm_pressed_and_doors_are_closed = false;
		disarm = false;
		siren_ends_and_doors_are_closed = false;
		door_opens = false;
		ac.reset();
	}

	@Action
	public void arm_pressed_and_doors_are_open() {
		state = State.DISARMED;
		ac.openDoors();
		Assert.assertEquals(0, ac.arm());
		Assert.assertEquals(1, ac.closeDoors());
		ac.reset();
		reset(true);
	}

	@Action
	public void arm_pressed_and_doors_are_closed() {
		state = State.ARMED;
		ac.closeDoors();
		Assert.assertEquals(1, ac.arm());
		reset(true);
		ac.reset();
	}

	@Action
	public void disarm() {
		state = State.DISARMED;
		ac.closeDoors();
		ac.arm();
		Assert.assertEquals(2, ac.disarm());
		ac.reset();
	}

	@Action
	public void disarm1() {
		state = State.DISARMED;
		ac.closeDoors();
		ac.arm();
		ac.openDoors();
		Assert.assertEquals(2, ac.disarm());
		ac.reset();
	}

	@Action
	public void siren_ends_and_doors_are_closed() {
		state = State.ARMED;
		ac.closeDoors();
		ac.arm();
		ac.openDoors();
		Assert.assertEquals(1, ac.closeDoors());
		ac.reset();
	}
	

	@Action
	public void door_opens() {
		state = State.ARMED;
		ac.closeDoors();
		ac.arm();
		Assert.assertEquals(27, ac.openDoors());
		ac.reset();
	}

	public boolean getArm_pressed_and_doors_are_open(boolean value) {arm_pressed_and_doors_are_open = value;
		return arm_pressed_and_doors_are_open;
	}
	public boolean getArm_pressed_and_doors_are_closed(boolean value) {arm_pressed_and_doors_are_closed = value;
		return arm_pressed_and_doors_are_closed;
	}
	public boolean getDisarm(boolean value) {disarm = value;
		return disarm;
	}
	public boolean getSiren_ends_and_doors_are_closed(boolean value) {siren_ends_and_doors_are_closed = value;
		return siren_ends_and_doors_are_closed;
	}
	public boolean getDoor_opens(boolean value) {door_opens = value;
		return door_opens;
	}

	public static void main(String[] args) {
		//Alarm ac = new Alarm();
		Tester tester = new GreedyTester(new AlarmSystemTesting());
		System.out.println("------------------------");
		tester.setRandom(new Random());
		CoverageMetric trCoverage = new TransitionCoverage(); // 5 transitions
		tester.addListener(trCoverage);
		CoverageMetric actionCoverage = new ActionCoverage(); // 5 actions
		tester.addListener(actionCoverage);
		CoverageMetric tpCoverage = new TransitionPairCoverage();
		tester.addListener(tpCoverage);
		CoverageMetric stCoverage = new StateCoverage(); // 3 states
		tester.addListener(stCoverage);
		tester.addListener("verbose"); // ask to print the generated tests
		int steps = 0;
		while (tpCoverage.getPercentage() < 100 /* || steps < 100 */) {
			tester.generate();
			steps++;
		}
		System.out.println("Generated " + steps + " steps.");
		tester.printCoverage();
	}

}
