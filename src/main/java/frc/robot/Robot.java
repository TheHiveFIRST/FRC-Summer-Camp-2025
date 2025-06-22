// // This comment explains the open source license for this software
// Open Source Software; you can modify and/or share it under the terms of

// // This comment continues the license info and specifies where to find the license file
// the WPILib BSD license file in the root directory of this project.

// // Declares the package name for this Java class
package frc.robot;

// // Import used to register sendable objects to SmartDashboard or other dashboards
import edu.wpi.first.util.sendable.SendableRegistry;

// // Import enum for motor types, used when creating Spark motor controllers
import com.revrobotics.spark.SparkLowLevel.MotorType;

// // Import SparkMax class for controlling REV SparkMax motor controllers
import com.revrobotics.spark.SparkMax;

// // Import SparkMaxConfig class for configuring SparkMax controllers
import com.revrobotics.spark.config.SparkMaxConfig;

// // Import for using a double solenoid (pneumatics)
import edu.wpi.first.wpilibj.DoubleSolenoid;

// // Import enum specifying pneumatics module type (like REV PH)
import edu.wpi.first.wpilibj.PneumaticsModuleType;

// // Import base class that calls periodic methods based on robot timing
import edu.wpi.first.wpilibj.TimedRobot;

// // Import class to access Xbox controller input
import edu.wpi.first.wpilibj.XboxController;

// // Import enum representing solenoid values (Forward, Reverse, Off)
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

// // Import class that allows differential drive logic (tank/arcade drive)
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

// // Import class to access the timer
import edu.wpi.first.wpilibj.Timer;

// // Define the main Robot class which extends the TimedRobot base class
public class Robot extends TimedRobot {

  // // Declare a timer to be used in autonomous mode
  private final Timer timer = new Timer();

  // // Declare the Xbox controller connected to port 0
  private final XboxController m_controller = new XboxController(0);

  // // Declare a double solenoid using module type REVPH and channels 0, 1
  private final DoubleSolenoid m_doubleSolenoid = new DoubleSolenoid(PneumaticsModuleType.REVPH, 0, 1);

  // // Declare first left-side motor using port 1 and brushed motor type
  private final SparkMax m_leftMotor1 = new SparkMax(1, MotorType.kBrushed);

  // // Declare second left-side motor using port 2 and brushed motor type
  private final SparkMax m_leftMotor2 = new SparkMax(2, MotorType.kBrushed);

  // // Declare third motor (right-side) using port 3
  private final SparkMax m_rightMotor3 = new SparkMax(3, MotorType.kBrushed);

  // // Declare fourth motor (right-side) using port 4
  private final SparkMax m_rightMotor4 = new SparkMax(4, MotorType.kBrushed);

  // // Configuration object for left-side motors
  private final SparkMaxConfig leftConfig = new SparkMaxConfig();

  // // Configuration object for right-side motors
  private final SparkMaxConfig rightConfig = new SparkMaxConfig();

  // // Declare a DifferentialDrive object to control the robot drive
  private final DifferentialDrive m_robotDrive;

  /** // This is a Javadoc comment for the constructor
   * Called once at the beginning of the robot program.
   */
  public Robot() {
    // // Set motor 2 to follow motor 1 without inversion
    leftConfig.follow(2, false);

    // // Set motor 4 to follow motor 3 without inversion
    rightConfig.follow(4, false);

    // // Apply left motor config to motor 1 (leader)
    m_leftMotor1.configure(leftConfig, null, null);

    // // Apply right motor config to motor 3 (leader)
    m_rightMotor3.configure(rightConfig, null, null);

    // // Create the DifferentialDrive using methods to set motor speeds
    m_robotDrive = new DifferentialDrive(m_leftMotor2::set, m_rightMotor4::set);

    // // Register left motor 2 with the dashboard for diagnostics
    SendableRegistry.addChild(m_robotDrive, m_leftMotor2);

    // // Register right motor 4 with the dashboard for diagnostics
    SendableRegistry.addChild(m_robotDrive, m_rightMotor4);
  }

  // // Method called once when autonomous mode starts
  @Override
  public void autonomousInit() {
    // // Reset the timer to 0
    timer.reset();

    // // Start the timer
    timer.start();

    // // Ensure the solenoid is initially retracted
    m_doubleSolenoid.set(Value.kReverse);
  }

  // // Method called periodically (every 20ms) during autonomous mode
  @Override
  public void autonomousPeriodic() {
    // // Get the current time since autonomous started
    double time = timer.get();

    // // If within the first 2 seconds
    if (time < 2.0) {
      // // Set right motor to 30% forward
      m_rightMotor4.set(0.3);

      // // Set left motor to 30% forward
      m_leftMotor2.set(0.3);

      // // Keep the solenoid retracted
      m_doubleSolenoid.set(Value.kReverse);
    } 
    // // If time is between 2 and 3 seconds
    else if (time >= 2.0 && time < 3.0) {
      // // Stop the right motor
      m_rightMotor4.set(0.0);

      // // Stop the left motor
      m_leftMotor2.set(0.0);

      // // Extend the solenoid
      m_doubleSolenoid.set(Value.kForward);
    } 
    // // If time is 3 seconds or more
    else {
      // // Keep right motor stopped
      m_rightMotor4.set(0.0);

      // // Keep left motor stopped
      m_leftMotor2.set(0.0);

      // // Retract the solenoid
      m_doubleSolenoid.set(Value.kReverse);
    }
  }

  // // Method called periodically during teleop mode (user-controlled)
  @Override
  public void teleopPeriodic() {
    // // Comment about using the controller: left stick to steer, triggers for throttle

    // // Call method to check solenoid control based on button input
    controlSolenoid();

    // // Perform arcade drive using controller input: left stick X to turn,
    // // right trigger forward, left trigger reverse
    m_robotDrive.arcadeDrive(-m_controller.getLeftX(), 
                              m_controller.getRightTriggerAxis() - m_controller.getLeftTriggerAxis());
  }

  // // Private method to control solenoid using Xbox buttons A and B
  private void controlSolenoid() {
    // // If A button was pressed this cycle
    if (m_controller.getAButtonPressed()) {
      // // Extend the solenoid
      m_doubleSolenoid.set(Value.kForward);
    } 
    // // Else if B button was pressed this cycle
    else if (m_controller.getBButtonPressed()) {
      // // Retract the solenoid
      m_doubleSolenoid.set(Value.kReverse);
    }
    // // If neither button was pressed
    else {
      // // Set solenoid to off (de-energized state)
      m_doubleSolenoid.set(Value.kOff);
    }
  }
}
