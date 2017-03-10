// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

//This is Ioannis's comment
//Comment from Ioannis's home pc
package org.usfirst.frc1735.Steamworks2017;

import org.usfirst.frc1735.Steamworks2017.commands.*;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    //// CREATING BUTTONS
    // One type of button is a joystick button which is any button on a joystick.
    // You create one by telling it which joystick it's on and which button
    // number it is.
    // Joystick stick = new Joystick(port);
    // Button button = new JoystickButton(stick, buttonNumber);

    // There are a few additional built in buttons you can use. Additionally,
    // by subclassing Button you can create custom triggers and bind those to
    // commands the same as any other Button.

    //// TRIGGERING COMMANDS WITH BUTTONS
    // Once you have a button, it's trivial to bind it to a button in one of
    // three ways:

    // Start the command when the button is pressed and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenPressed(new ExampleCommand());

    // Run the command while the button is being held down and interrupt it once
    // the button is released.
    // button.whileHeld(new ExampleCommand());

    // Start the command when the button is released  and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ExampleCommand());


    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public JoystickButton autonomousExperimentRun;
    public JoystickButton centerOnGearButton;
    public Joystick joyLeft;
    public JoystickButton toggleDrivetrainButton;
    public JoystickButton forceMecanumButton;
    public JoystickButton resetGyroButton;
    public Joystick joyRight;
    public JoystickButton fireButton;
    public JoystickButton shooterStartButton;
    public JoystickButton shooterStopButton;
    public JoystickButton climbUp;
    public JoystickButton climbDown;
    public Joystick operator;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    public OI() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS

        operator = new Joystick(2);
        
        climbDown = new JoystickButton(operator, 5);
        climbDown.whileHeld(new ClimbRope());
        climbUp = new JoystickButton(operator, 4);
        climbUp.whileHeld(new ClimbRope(1.0));
        shooterStopButton = new JoystickButton(operator, 3);
        shooterStopButton.whenPressed(new ShooterStop());
        shooterStartButton = new JoystickButton(operator, 2);
        shooterStartButton.whenPressed(new ShooterStart());
        fireButton = new JoystickButton(operator, 1);
        fireButton.whileHeld(new FeederStart(1));
        joyRight = new Joystick(1);
        
        resetGyroButton = new JoystickButton(joyRight, 8);
        resetGyroButton.whenPressed(new ResetGyro());
        forceMecanumButton = new JoystickButton(joyRight, 6);
        forceMecanumButton.whileHeld(new ForceMecanumMode());
        toggleDrivetrainButton = new JoystickButton(joyRight, 2);
        toggleDrivetrainButton.whenPressed(new ToggleDrivetrain());
        joyLeft = new Joystick(0);
        
        centerOnGearButton = new JoystickButton(joyLeft, 1);
        centerOnGearButton.whenPressed(new CenterOnGearPeg());
        autonomousExperimentRun = new JoystickButton(joyLeft, 2);
        autonomousExperimentRun.whenPressed(new AutonomousDoNothing());


        // SmartDashboard Buttons
        SmartDashboard.putData("AutonomousHopperAndShoot", new AutonomousHopperAndShoot());
        SmartDashboard.putData("CenterOnBoiler", new CenterOnBoiler());
        SmartDashboard.putData("ActivateTurret", new ActivateTurret());
        SmartDashboard.putData("ResetGyro", new ResetGyro());
        SmartDashboard.putData("BoilerCameraLight: off", new BoilerCameraLight(false));
        SmartDashboard.putData("BoilerCameraLight: on", new BoilerCameraLight(true));
        SmartDashboard.putData("GearCameraLight: off", new GearCameraLight(false));
        SmartDashboard.putData("GearCameraLight: on", new GearCameraLight(true));

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        
        SmartDashboard.putData("Experimental GearVision routine", new AutonomousCommand());
        //--------------------------------------
        // Additional User-defined functionality
        //--------------------------------------
        // This button sets the master debug variable.
        // It can be dynamically set during robot operation to turn on/off debug messages!
        SmartDashboard.putBoolean("Master Debug Enable", false);
        
        // Initialize the "camera light status" indicator on the SmartDashboard.  It only updates when the command is called.
        SmartDashboard.putBoolean("Boiler Camera Light On", false);
        
        // Initialize "Demo Mode" variable for the SmartDashboard.
        // This is used to turn off joysticks, etc. at demo events (to avoid having to pull fuses)
        SmartDashboard.putBoolean("Demo Mode", false);
       
    }

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=FUNCTIONS
    public Joystick getJoyLeft() {
        return joyLeft;
    }

    public Joystick getJoyRight() {
        return joyRight;
    }

    public Joystick getOperator() {
        return operator;
    }


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=FUNCTIONS
}

