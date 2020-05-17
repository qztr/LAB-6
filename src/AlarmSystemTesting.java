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
	private Random rand = new Random();
	//private AlarmSystemTesting ac = new AlarmSystemTesting(); // the SUT (MBT.AlarmClockImpl.jar)
	private Alarm ac = new Alarm();
	private State state; // enum defined below
//	private boolean alarmOnTime; // Is it time for the alarm to ring
//	private boolean alarmCancel; // Is the alarm currently cancelled
//	private boolean alarmSet; // Is the alarm currently set
	private boolean arm_pressed_and_doors_are_open;
	private boolean arm_pressed_and_doors_are_closed;
	private boolean disarm;
	private boolean siren_ends_and_doors_are_closed;
	private boolean door_opens;
	
	
	 
	private enum State {
		DISARMED, ARMED, SIREN;
		// DISARMED, ALARM_SET, ALARM_RINGING;
	}

	@Override
	public Object getState() {
		return (String.valueOf(state));
	}

//	@Override
//	public void reset(boolean testing) {
//		state = State.DISARMED;
//	}
	
	
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

//	public boolean setIdleGuard() {
//		return (state == State.DISARMED && !getSet(false) && !getTime(false) && !getCancel(false));
//	}

	@Action
	public void arm_pressed_and_doors_are_open() {
		state = State.DISARMED;
		if (ac.openDoors() == 0) {
			Assert.assertEquals(0,ac.arm());
		}
		ac.reset();
		reset(true);
		//Assert.assertEquals(0,ac.arm());
		//Assert.assertEquals(String.valueOf(state), ac.Alarm(alarmSet, alarmOnTime, alarmCancel));
	}

//	public boolean setAlarmGuard() {
//		return (state == State.DISARMED && getSet(true) && !getTime(false) && !getCancel(false));
//	}

	@Action
	public void arm_pressed_and_doors_are_closed() {
		state = State.ARMED;
		if (ac.closeDoors() > 0) {
			Assert.assertEquals(1,ac.arm());
			Assert.assertEquals(27,ac.openDoors());	
		}
		reset(true);
		ac.reset();
		//Assert.assertEquals(String.valueOf(state), ac.Alarm(alarmSet, alarmOnTime, alarmCancel));
		//Assert.assertEquals(0,ac.disarm());
	}

//	public boolean cancelAlarmGuard() {
//		return (state == State.ALARM_SET && getSet(true) && !getTime(false) && getCancel(true));
//	}

	@Action
	public void disarm() {
		state = State.DISARMED;
		if (ac.arm() == 0){
			Assert.assertEquals(2,ac.disarm());
		}
		ac.reset();
		//Assert.assertEquals(String.valueOf(state), ac.Alarm(alarmSet, alarmOnTime, alarmCancel));
	}

//	public boolean alarmOnTimeGuard() {
//		return (state == State.ALARM_SET && getSet(true) && getTime(true) && !getCancel(false));
//	}

	@Action
	public void siren_ends_and_doors_are_closed() {
		state = State.ARMED;
		
		//Assert.assertEquals(String.valueOf(state), ac.Alarm(alarmSet, alarmOnTime, alarmCancel));
	}

//	public boolean alarmOffGuard() {
//		return (state == State.ALARM_RINGING && getSet(true) && getTime(true) && getCancel(true));
//	}
	
//	public boolean doors_opensGuard() {
//		return (state == State.ARMED && getDoor_opens(true));
//	}

	@Action
	public void door_opens() {
		state = State.ARMED;
		if(ac.closeDoors() == 0) {
			ac.arm();
			System.out.println("armssssssssssssssssss ");
			Assert.assertEquals(27,ac.openDoors());
		}
		ac.reset();
		
		if(ac.openDoors() == 0) {
			ac.disarm();
			System.out.println("armsssssssAAAAAa ");
			ac.arm();
			Assert.assertEquals(27,ac.openDoors());
		}
		ac.reset();
		
		//Assert.assertEquals(String.valueOf(state), ac.Alarm(alarmSet, alarmOnTime, alarmCancel));
		//this.reset(false);
	}

//	public boolean getTime(boolean value) {
//		alarmOnTime = value;
//		return alarmOnTime;
//	}
//
//	public boolean getCancel(boolean value) {
//		alarmCancel = value;
//		return alarmCancel;
//	}
//
//	public boolean getSet(boolean value) {
//		alarmSet = value;
//		return alarmSet;
//	}
	

	
	public boolean getArm_pressed_and_doors_are_open(boolean value) {
		arm_pressed_and_doors_are_open = value;
		return arm_pressed_and_doors_are_open;
	}
	
	public boolean getArm_pressed_and_doors_are_closed(boolean value) {
		arm_pressed_and_doors_are_closed = value;
		return arm_pressed_and_doors_are_closed;
	}
	
	public boolean getDisarm(boolean value) {
		disarm = value;
		return disarm;
	}
	
	public boolean getSiren_ends_and_doors_are_closed(boolean value) {
		siren_ends_and_doors_are_closed = value;
		return siren_ends_and_doors_are_closed;
	}
	
	public boolean getDoor_opens(boolean value) {
		door_opens = value;
		return door_opens;
	}

	public static void main(String[] args) {
		Alarm ac = new Alarm();
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
		
		
//		System.out.println("arm " + ac.arm());
//		//System.out.println("disarm " + ac.disarm());
//		System.out.println("closeDoors " + ac.closeDoors());
//		System.out.println("openDoors " + ac.openDoors());
//		System.out.println("disarm " + ac.disarm());
//		System.out.println("closeDoors " + ac.closeDoors());
//		
		ac.reset();
		//System.out.println("arm " + ac.arm());
		System.out.println("disarm " + ac.disarm());
		System.out.println("disarm " + ac.openDoors());
		System.out.println("arm " + ac.arm());
		System.out.println("disarm " + ac.openDoors());
		

		
	}

}
