// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc1735.Steamworks2017.commands;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc1735.Steamworks2017.Robot;

/**
 *
 */
public class Turn extends Command {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
    private int m_mode;
    private double m_speed;
    private double m_angle;
 
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
    // Manual variables
    // Get the initial (reference) gyro angle
    double m_initialGyroAngle;
    double m_targetGyroAngle;

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public Turn(int mode, double speed, double angle) {

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        m_mode = mode;
        m_speed = speed;
        m_angle = angle;

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        
        
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.driveTrain);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	// Assume for now we will never need to turn for more than a second.
    	//This protects us against a faulty gyro preventing us from believing we reached the desired angle
    	setTimeout(10);
    	
    	System.out.println("Issuing turn in mode " + m_mode + " and speed " + m_speed + "for " + m_angle + " degrees.");

    	// Get the initial gyro angle, converted to a 0-360 (modulo 360) value
    	// (getAngle is cumulative, so if you have turned 10 times to the left, the value is -3600)
    	m_initialGyroAngle = Robot.ahrs.getAngle()%360;
    	System.out.println("modulo gyro value at init is" + m_initialGyroAngle);
    	
    	// Calculate the target gyro value (mod360), based on the desired rotation angle
    	m_targetGyroAngle = (m_initialGyroAngle + m_angle)%360;
    	System.out.println("based on desired rotaton of " + m_angle + ", modulo target angle is " + m_targetGyroAngle);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	// Implement as non-PID for now
    	// execute a turn based on the input mode and requested rotation.
    	if (m_mode == 0) { // mecanum; use enum later
    		Robot.driveTrain.mecanumDrive(0, 0, m_speed); // x,y,rot
    	}
    	else if (m_mode == 1) { // Traction; use enum later
    		Robot.driveTrain.arcadeDrive(0, m_speed);// move, rot
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        boolean done;
        double currentAngle = Robot.ahrs.getAngle()%360; // convert to 0-360 because total angle is cumulative
    	System.out.println("Current modulo gyro value is" + currentAngle);
    	done = (((m_angle <= 0) && (currentAngle <= m_targetGyroAngle)) || // we are turning left  and just got equal/smaller than our target
    			((m_angle >= 0) && (currentAngle >= m_targetGyroAngle)));  // we are turning right and just got equal/bigger  than our target
    	return (done || isTimedOut());
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.driveTrain.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
