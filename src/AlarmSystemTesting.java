import java.util.Random;
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

public class AlarmSystemTesting implements FsmModel {
	//private Random rand = new Random();
	//private AlarmSystemTesting ac = new AlarmSystemTesting(); // the SUT (MBT.AlarmClockImpl.jar)
	private State state; // enum defined below
//	private boolean alarmOnTime; // Is it time for the alarm to ring
//	private boolean alarmCancel; // Is the alarm currently cancelled
//	private boolean alarmSet; // Is the alarm currently set
//	private boolean arm_pressed_and_doors_are_open;
//	private boolean arm_pressed_and_doors_are_closed;
//	private boolean disarm;
//	private boolean siren_ends_and_doors_are_closed;
//	private boolean door_opens;

	private enum State {
		DISARMED, ARMED, SIREN;
		// DISARMED, ALARM_SET, ALARM_RINGING;
	}

	@Override
	public Object getState() {
		return (String.valueOf(state));
	}

	@Override
	public void reset(boolean testing) {
		state = State.DISARMED;
	}
	
	
//	@Override
//	public void reset(boolean testing) {
//		state = State.DISARMED;
//		arm_pressed_and_doors_are_open = false;
//		arm_pressed_and_doors_are_closed = false;
//		disarm = false;
//		siren_ends_and_doors_are_closed = false;
//		door_opens = false;
//		// ac.reset();
//	}

//	public boolean setIdleGuard() {
//		return (state == State.DISARMED && !getSet(false) && !getTime(false) && !getCancel(false));
//	}

	@Action
	public void arm_pressed_and_doors_are_open() {
		state = State.DISARMED;
		//Assert.assertEquals(String.valueOf(state), ac.Alarm(alarmSet, alarmOnTime, alarmCancel));
	}

//	public boolean setAlarmGuard() {
//		return (state == State.DISARMED && getSet(true) && !getTime(false) && !getCancel(false));
//	}

	@Action
	public void arm_pressed_and_doors_are_closed() {
		state = State.ARMED;
		//Assert.assertEquals(String.valueOf(state), ac.Alarm(alarmSet, alarmOnTime, alarmCancel));
	}

//	public boolean cancelAlarmGuard() {
//		return (state == State.ALARM_SET && getSet(true) && !getTime(false) && getCancel(true));
//	}

	@Action
	public void disarm() {
		state = State.DISARMED;
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

	@Action
	public void door_opens() {
		state = State.SIREN;
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

	public static void main(String[] args) {
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
