// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc1735.Steamworks2017.subsystems;

import org.usfirst.frc1735.Steamworks2017.Robot;
import org.usfirst.frc1735.Steamworks2017.RobotMap;
import org.usfirst.frc1735.Steamworks2017.commands.*;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class GearVision extends PIDSubsystem {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private final AnalogGyro fakeCameraGyro = RobotMap.gearVisionFakeCameraGyro;
    private final SpeedController fakeMotor = RobotMap.gearVisionFakeMotor;
    private final Solenoid cameraLightRelay = RobotMap.gearVisionCameraLightRelay;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    //-------------------------
    // General approach/design
    // ------------------------
    // 
    // This is a particulary tough subsystem because it wants to:
    // 1) Use a camera as the "input sensor", which is inherently both laggy and low bandwidth
    //    (30 frames/sec is about 30ms, which is even slower than the DriverStation loop rate.
    // 2) Use the drivetrain as the output, which is inherently laggy, sloppy, and difficult to start/stop
    //    due to momentum/inertia, gear backlash, slippage, etc
    // 3) The motion required to line up to the gear peg requires CRAB sideways motion,
    //    which is the most difficult to model/predict/control
    // 4) The gear spoke openings are only about 3" for spearing a 1" peg, which gives only about an inch of freedom.
    //    The mechanism has to be exceptionally accurate despite the items above...
    //
    // No problem.  :-)
    // Some approaches to minimize these issues (it certainly doesn't eliminate them) is to:
    // a) Snapshot the camera only for defining the setpoint, and not for continuous feedback
    // b) Use the encoders for continuous feedback because they update at a much faster rate
    //    and are directly tied to the motors they influence
    // c) Avoid using the software PIDSubsystem (but extend from it anyway as a mitigation to (d) below.
    // d) Use the built-in position PID of the motor controller to improve the feedback loop even more
    //    (This is tough for mecanum, but if we limit to pure crab we can run the four wheels in lockstep 
    //     with one master and three slaves w/ appropriate inversions)
    // e) Use the slowest reasonable speed to avoid slippage
    // f) Avoid integral windup:   Since motors don't like to output very small values,
    //    we have a real issue with being "very close" to the setpoint.
    //    Tuning "I" doesn't help because we integrate a large amount of error at this low output,
    //    and then reach the nonlinear part of the motor curve and then overshoot massively.   
    // g) Use both PID profiles:  Actually three would be ideal:
    //    i) One for larger distances, where the P term dominates and there are minimal restrictions
    //       (hopefully we are "close" to target by our autonomous driving, and can eliminate this range
    //    ii) For medium errors, we increase the P but begin to limit the max output.  This effectively
    //        increases the P power to overcome the motor resistance to startup, but the output clamp
    //        prevents us from going too fast and over-shooting the setpoint.
    //    iii) for very small errors, really crank up the P to force the motor to activate, but again limit
    //         the output (to an even smaller value) so we are guaranteed to move, but not to overshoot.
    //    One possible set of ranges (purely as an example):
    //    + If the error is greater than 10 inches, use a PID of .8/0/.01, and max_output=1 (or perhaps just 0.5 to cover point (d) above)
    //    + if the error is 3-10 inches, use P=4 and max_output=.375
    //    + if the error is < 3 inches, use P=10 and max_output of 0.2
    
    
    // Initialize your subsystem here
    public GearVision() {
    	// SW PID currently unused; only a fallback strategy
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PID
        super("GearVision", 1.0, 0.0, 0.0);
        setAbsoluteTolerance(0.2);
        getPIDController().setContinuous(false);
        LiveWindow.addActuator("GearVision", "PIDSubsystem Controller", getPIDController());
        getPIDController().setOutputRange(-0.5, 0.5);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PID

        // Use these to get going:
        // setSetpoint() -  Sets where the PID controller should move the system
        //                  to
        // enable() - Enables the PID controller.
        
        // Populate the centerpoint value on the SmartDashboard (for tuning)
        SmartDashboard.putNumber("Gear Center Offset (pixels)", m_targetCenterOffset);
        
	    // Get a pointer to the networkTable.  "GearVision" is the name we entered into the publish box in GRIP
	    m_gearTable = NetworkTable.getTable("GRIP/GearVision");
        System.out.println("GearVision table handle is " + m_gearTable);
        
    }
    

    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    // Currently unused
    protected double returnPIDInput() {
        // Return your input value for the PID loop
        // e.g. a sensor, like a potentiometer:
        // yourPot.getAverageVoltage() / kYourMaxVoltage;

        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=SOURCE
        return fakeCameraGyro.pidGet();

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=SOURCE
    }

    // Currently unused
   protected void usePIDOutput(double output) {
        // Use output to drive your system, like a motor
        // e.g. yourMotor.set(output);

        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=OUTPUT
        fakeMotor.pidWrite(output);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=OUTPUT
    }
    
    // Returns true if we are actively using the gearVision controller (enables vision snapshots and the HW PID controller)
   // @FIXME:  Looks like this is unused?
    public boolean isVisionEnabled() {
    	return m_visionEnabled;
    }
    
    // Set the mode (active = true)
    public void setVisionEnable(boolean isEnabled) {
    	m_visionEnabled = isEnabled;
    	cameraLightOn(isEnabled); // enable the camera light whenever the vision system is enabled.
    }
        
    // Return if we think the PID is at its target setpoint right now
    public boolean onTarget() {
    	return Robot.driveTrain.gearOnTarget();
    }
    
    //----------------------------------
    // getErrorDistance() in inches
    //----------------------------------
    // This is the "meat" of the vision processing system.
    // Grab the current "centered X" position stuffed into the NetworkTable by the coprocessor,
    // and calculate how far off-center we are.
    public double getErrorDistance() {
    	// First, find out how far off center we are in the image.
    	// The desired centerline is Xres/2 + our compensation for camera vs gear location
    	// Get the current setpoint from SmartDashboard (so it can be tuned without recompiling)
    	double desiredCenter = m_xRes/2 + SmartDashboard.getNumber("Gear Center Offset (pixels)", m_targetCenterOffset); // Default to the compiled value if entry not found
    	
    	// Get the raw position and object width from NetworkTables:
    	double [][] rawData = getRawTargetData();
    	double xPos[] = rawData[0]; // Target offset from center in pixels
    	double xWidArr[] = rawData[1]; // Target width in pixels
    	
    	// The width of the full rectangle defined by the two tape targets is the difference in centerpoints of the two targets, plus half the width of the left and right targets.
    	double xWid = Math.abs(xPos[1]-xPos[0]) + xWidArr[0]/2 + xWidArr[1]/2;
    	// The center of the full rectangle is the average of the two centerpoints:
    	double imageCenter = (xPos[0] + xPos[1])/2;
    	// (Alternatively, could do a difference between them and add to the left centerpoint, but that's not as simple.)
    	//double imageCenter = (Math.abs(xPos[1]-xPos[0])/2 + Math.min(xPos[0],  xPos[1]));
    	double imageErrorDistance = imageCenter - desiredCenter; // Make errors to the right of center be positive
    	
    	// Using similar triangles and ratios...
    	// The ratio of the (camera image of the target width in pixels) to the (real width in inches) 
    	// should be the same as the ratio of the (camera image center offset ["error distance"] in pixels) vs (offset in inches)
    	// Therefore:
    	double inchErrorDistance;
    	if (xWid==0)
    		inchErrorDistance = 0;
    	else
    		inchErrorDistance = ((imageErrorDistance* m_targetWidthInches)/xWid);
    	System.out.println("Calculated error in inches is " + inchErrorDistance + "desired center " + desiredCenter + "Image center" + imageCenter + "width " + xWid + "Image error in pix " + imageErrorDistance);
    	
    	// Find the distance
    	double distance = calculateDistanceFromCamera(xWid);
    	System.out.println("distance = " + distance);
    	return inchErrorDistance;
    	
    }
    
    // Return data for a single contour (the Table presents an array of each piece of data, unfortunately... have to turn it sideways
    // [0]: the pixel position of the center of the target's X axis
    // [1]: the width of the target (total width)
    public double[][] getRawTargetData() {
    	double xPos[];// = new double {0,0};
    	double xWid[];// = new double {0,0}; // components of our return value

    	xPos = new double[] {0,0};
    	xWid = new double[] {0,0};
    	
    	// 1) Get the current list of targets found.  There might be more than one visible at a time if our processing is noisy,
    	//    or if we can't combine the two tape strips into a single blob on the coprocessor
       	// First step: get the vision system data for the target

    	// Get all needed table items at (roughly) the same time, to minimize table updates between reads.
    	// (could end up with different array sizes)
    	double[] defaultValue = new double[] {0,0}; // set up a default value in case the table isn't published yet
    	double[] targetX = m_gearTable.getNumberArray("centerX", defaultValue);    	
		double[] width = m_gearTable.getNumberArray("width", defaultValue);
    	// For initial debug, just print out the table so we can see what's going on

		int a = targetX.length;
		int b = width.length;
		System.out.println("targetX length is " + a + " and width length is "+ b);
    	System.out.print("Raw centerX: ");
    	for (double xval : targetX) { // for each target found,
    		System.out.print(xval + " ");
    	}
    	System.out.print(" Raw width: ");
    	for (double xwid : width) { // for each target found,
    		System.out.print(xwid + " ");
    	}
    	System.out.println("");

		
		if (targetX.length != width.length) {
			// here the table updated in the middle; we'll have to punt.
			// (Yes, it could have updated to the same number of objects, but different objects.  There is no way to detect that at all)
			// This is just to indicate noise where the number of identified contours is varying rapidly, possibly between none and one.
			System.out.println("NetworkTable udpated in the middle of getRawTargetData; may have inconsistent datapoints!");
		}
		// Choose the first object, if one was found.
    	if (targetX.length<2) {
    		// We didn't find a valid x position to use.
    		// Return a perfectly centered answer so that the system doesn't try to adapt
    		// We also have to compensate for offset between the camera image and the actual robot.
    		System.out.println("Punting on XPos!");
    		xPos[0] = (((m_xRes/2) + SmartDashboard.getNumber("Gear Center Offset (pixels)", m_targetCenterOffset))) - 39;
    		xPos[1] = (((m_xRes/2) + SmartDashboard.getNumber("Gear Center Offset (pixels)", m_targetCenterOffset))) + 39;
    	}
    	else {
    		// Grab only the first two (in case there are >2?)
    		xPos[0] = targetX[0];
    		xPos[1] = targetX[1];
    	}
    	if (width.length<2) {
    		// No valid width.  punt and set the width to some value that matches what we'd see from our desired starting position...
    		System.out.println("Punting on xWid!");
    		xWid[0] = 22; // This is an arbitrary choice until we measure it.
    		xWid[1] = 22;
    	}
    	else {
    		 // Grab only the first two (in case there are >2?)
    		xWid[0] = width[0];
    		xWid[1] = width[1];
    	}
	    	
	    	
    	System.out.print("\nCalculated xPos and xWid: ");
    	for (int i=0; i<=1; i++) {
    		System.out.print("xPos[" + i + "] = " + xPos[i] + " xWid[" + i + "] = " + xWid[i]);
    	}
    	System.out.println();
   	
    	
	    // Return an array of the answers
	    double[][] rawData = {{xPos[0], xPos[1]}, {xWid[0], xWid[1]}};
	    return rawData;
     }

    public double calculateDistanceFromCamera(double targetWidthPixels) {
    	//implements this equation:
    	// d = Tinches*FOVpixel/(2*Tpixel*tan(CameraTheta))
    	// where FOVpixel is represented by m_xRes.
    	double distance, manualDistance;
    	
    	// If we don't see a target, the width will be zero.
    	// Punt by setting the distance to be something in the middle...
    	// choose the most likely distance for autonomous, so a failure of the camera still has a good chance of working.
    	// Something like 10" sounds good.
    	if (targetWidthPixels > 0) {
    		// @FIXME:  last year the math gave the wrong answer, and we had to hand-calculate the number.
    		// print both for debug purposes until we know this is resolved...
    		distance = (m_targetWidthInches*m_xRes)/(2*targetWidthPixels*Math.tan(Math.toRadians(m_cameraTheta)));
    		distance = distance *1.215; // this is an empirical fudge factor to make the distance work ok for 68", which is the target driving distance.  
    		// Now calculate based on manual analysis of the above data:
    		// (10.2*640)/(2*Tpx*tan(30.521)) = 
    		manualDistance = (5536.5312/targetWidthPixels)*1.215;
    		Robot.dbgPrintln("For input width " + targetWidthPixels + "\tCalculated distance is " + distance + "\tManually calculated distance is " + manualDistance);
    	}
    	else {
    		distance = 69; //69" is the expected driving distance... an arbitrary but hopefully reasonable value for "punting"...
    		Robot.dbgPrintln("No target found; punting with default value of " + distance); 
    	}
    	return distance;
    }

    public void cameraLightOn(boolean onState) {
    	// If onState is true, turn the camera light relay on.
    	// Otherwise, turn it off.
    	cameraLightRelay.set(onState);
    	// Print the new state of the light to the SmartDashboard
		SmartDashboard.putBoolean("Gear Camera Light On", onState);
    }
    
    // Backup plan:  USe vision to get the error distance, and use DriveWithProgram to move the desired amount.
    // To do this, we need to store off the requested distance
    public void setErrorDistanceVal(double errorDistance) {
    	m_errorDistance = errorDistance;
    }
    public double getErrorDistanceVal() {
    	return m_errorDistance;
    }
    
    //----------------------------------
    // Member Variables
    //----------------------------------
    private boolean m_visionEnabled = false; // Is the Gear vision system active?
	NetworkTable m_gearTable;
	
	
    //----------------------------------
    // Constants
    //----------------------------------
	public static final double m_xRes = 640; // this is the maximum resolution in pixels for the x (horizontal) direction-- determined by how the vision pipeline is implemented.
	private static double m_cameraTheta = 30.521; // Empirically determined for Microsoft LifeCam3000 for 2017
	private static double m_targetWidthInches = 10.2; //Target strips are 2" wide with a 6.2" separation between the inside edges
	// This is the offset (in PIXELS) that we need to compensate between the camera center and the robot shooting centered.
	// You can determine this empirically by getting the robot to shoot perfectly and then reading the raw Xpos from the vision system...
	// (This can be overridden by the SmartDashboard)
	private static double m_targetCenterOffset = -59;    //(+1 means the robot is really centered when the image center is 1 pixel to the right of dead center xRes/2)
	private double m_errorDistance = 0;

}
