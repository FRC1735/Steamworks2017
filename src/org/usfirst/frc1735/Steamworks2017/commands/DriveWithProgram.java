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
import org.usfirst.frc1735.Steamworks2017.RobotMap;
import org.usfirst.frc1735.Steamworks2017.subsystems.DriveTrain;
import org.usfirst.frc1735.Steamworks2017.subsystems.DriveTrain.DrivetrainMode;

/**
 *
 */
public class DriveWithProgram extends Command {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
 
    // END AUTOGENERATED CODE,[ SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
	 // Member variables set by constructor
	private DrivetrainMode m_mode;
	private double m_driveMagDir;
	private double m_driveDist;
	private double m_turnAngle; // PID determines magnitude
	private double m_crabMagDir;
	private double m_crabDist;
	
	// Member variables set by initialize()
	private double m_FLStartDistance; // Front Left encoder starting value
	private double m_FRStartDistance; // Front Right
	private double m_BLStartDistance; // Back Left
	private double m_BRStartDistance; // Back Right
	private double m_initialGyroAngle; // Starting Gyro value
	private double m_targetGyroAngle;  // Calculated target gyro angle

	// Default constructor (sometimes this is needed by the compiler)
	public DriveWithProgram() {
		this	(DriveTrain.DrivetrainMode.kMecanum,
				1,   // Timelimit 1 sec
				0,0, // Drive mag/dist
				0,0, // Crabmag/dist
				0);  // Turn angle (PID determines magnitude)
			
	}
	
	// The full constructor with all the trimmings...
    public DriveWithProgram(DriveTrain.DrivetrainMode mode,
    						double timeLimit,
    						double driveMagDir, double driveDist,
    						double crabMagDir,  double crabDist,
    						double turnAngle) { // PID determines magnitude

        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.driveTrain);

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        
        // Do the construction-time initialization
        m_mode = mode;
        setTimeout(timeLimit);
        m_driveMagDir = driveMagDir;
        m_driveDist = driveDist;
        m_turnAngle = turnAngle;
        m_crabMagDir = crabMagDir;
        m_crabDist = crabDist;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	// Sanity check the args.
    	// For instance, setting mode to traction and asking for Crab is not legal
    	if ((m_mode == DriveTrain.DrivetrainMode.kTraction) &&
    		(m_crabMagDir != 0)) {
    		System.err.println("Call to DriveWithProgram with mode=traction and nonzero Crab!  Ignoring crab value.");
    		m_crabMagDir = 0;
    	}
    	// Also, distance is assumed to be positive.  *DirMag determines direction
    	if (m_driveDist < 0) {
    		System.err.println("Negative driveDist provided.  Converting to positive.");
    		m_driveDist = -m_driveDist;
    	}
    	if (m_crabDist < 0) {
    		System.err.println("Negative crabDist provided.  Converting to positive.");
    		m_crabDist = -m_crabDist;
    	}
    	
    	// Get the initial values for all gyros
    	m_FLStartDistance = RobotMap.driveTrainFLMotor.getEncPosition();
    	m_FRStartDistance = RobotMap.driveTrainFRMotor.getEncPosition();
    	m_BLStartDistance = RobotMap.driveTrainBLMotor.getEncPosition();
    	m_BRStartDistance = RobotMap.driveTrainBRMotor.getEncPosition();
    	
    	// Get the initial Gyro heading
    	m_initialGyroAngle = Robot.ahrs.getAngle();

    	// Calculate our PID target for heading.
    	// If we are just driving straight, the turnAngle will be zero.  Maintain initial heading.
     	// If we are asked to turn, the turnAngle will be nonzero, and we will move to a new heading.
    	// Either way, the calculation of the target angle is the same:
    	m_targetGyroAngle = (m_initialGyroAngle + m_turnAngle);
    	
    	// Finally, enable the turn controller
    	Robot.driveTrain.drivelineController.setSetpoint(m_targetGyroAngle);	
    	Robot.driveTrain.drivelineController.enable();
   	
   }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
       	// Use mode bit to determine which driveline mode to use to accomplish the PID output reaction
    	if (m_mode == DriveTrain.DrivetrainMode.kMecanum) {
    		Robot.driveTrain.mecanumDrive(m_crabMagDir, m_driveMagDir, Robot.driveTrain.getPIDRotationRate()); // x,y,rot
    	}
    	else if (m_mode == DriveTrain.DrivetrainMode.kTraction) {
    		Robot.driveTrain.arcadeDrive(m_driveMagDir, Robot.driveTrain.getPIDRotationRate());// move, rot
    	}

    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	boolean timedOut = isTimedOut();
    	
        // Because we have multiple degrees of freedom that may be requested, we don't finish until ALL of them are achieved
    	// 1) Get state of drive limit
    	//    There are four encoders.  Claim victory if any of them reached the limit (Courtesy of the Department of Redundancy Department)
    	//@FIXME:  How do we code in the distance_per_tick for this encoder type?
    	double FLCurrentDistance = RobotMap.driveTrainFLMotor.getEncPosition();
    	double FRCurrentDistance = RobotMap.driveTrainFRMotor.getEncPosition();
    	double BLCurrentDistance = RobotMap.driveTrainBLMotor.getEncPosition();
    	double BRCurrentDistance = RobotMap.driveTrainBRMotor.getEncPosition();

    	// Determine amount of travel
    	double FLTravel = Math.abs(FLCurrentDistance - m_FLStartDistance);
    	double FRTravel = Math.abs(FRCurrentDistance - m_FRStartDistance);
    	double BLTravel = Math.abs(BLCurrentDistance - m_BLStartDistance);
    	double BRTravel = Math.abs(BRCurrentDistance - m_BRStartDistance);
    	
    	// From travel, determine if we reached the drive distance limit on ANY encoder.
    	//@FIXME:  We may need to change this to use only one encoder if we are turning, as some wheels might go slow or even backwards
    	boolean driveDistReached = ((FLTravel >= m_driveDist) || // Note 2016 used > and not >=
    								(FRTravel >= m_driveDist) ||
    								(BLTravel >= m_driveDist) ||
    								(BRTravel >= m_driveDist));
    	// From travel, determine if we reached the crab distance limit
    	// This is tougher because wheels spin in opposite directions!
    	// Therefore the distance_per_revolution may be different.
    	//@FIXME:  empirically determine what the factors are for crab vs forward
    	boolean crabDistReached = (	(FLTravel > m_crabDist) ||
    								(FRTravel >= m_crabDist) ||
    								(BLTravel >= m_crabDist) ||
    								(BRTravel >= m_crabDist));
    	
    	// IF we specified no angle, then the PID was trying to keep us on a constant heading an onTarget() is still valid to look at.
    	boolean angleReached = Robot.driveTrain.drivelineController.onTarget();
    			
    	return (timedOut || (driveDistReached && crabDistReached && angleReached));
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.driveTrain.drivelineController.disable(); // Stop the turn controller
    	Robot.driveTrain.stop(); // Stop the motors
    	
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
