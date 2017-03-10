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

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc1735.Steamworks2017.subsystems.*;

/**
 *
 */
public class CenterOnGearPeg extends CommandGroup {


    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PARAMETERS
    public CenterOnGearPeg() {

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PARAMETERS
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=COMMAND_DECLARATIONS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=COMMAND_DECLARATIONS
    	addSequential(new GearCameraLight(true));
    	addSequential(new Delay(0.5)); // Give a moment for camera to pick up on lit image
    	addSequential(new SetGearPegErrorDistance()); // save off the gear error
    	
    	// Use the drive routine to crab the amount determined by the error calculated above
    	addSequential(new DriveWithProgram(DriveTrain.DrivetrainMode.kMecanum,
    										3, // timeout
    										0,0, // drive MagDir, dist
    										0,0, // crab:  Data will come from SetGearPegErrorDistance() above!
    										0)); // Angle to turn
    	// Turn off gearvision overrides and camera light
    	addSequential(new ClearGearVision());
    	
    	// At this point, resume normal operation.

    } 
}
