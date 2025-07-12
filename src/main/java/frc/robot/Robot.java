package frc.robot;

import edu.wpi.first.util.sendable.SendableRegistry;
// // Import enum for motor types, used when creating Spark motor controllers
import com.revrobotics.spark.SparkLowLevel.MotorType;

import frc.robot.PivotSubsystem;
import frc.robot.Constants.pivotConstants;

import java.lang.ModuleLayer.Controller;

// // Import SparkMax class for controlling REV SparkMax motor controllers
import com.revrobotics.spark.SparkMax;

// // Import SparkMaxConfig class for configuring SparkMax controllers
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.motorcontrol.Spark; // This import is for the Spark PWM controller
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

public class Robot extends TimedRobot {
  private final Timer timer = new Timer();
  private final XboxController m_controller = new XboxController(0);
  private final DoubleSolenoid m_doubleSolenoid = new DoubleSolenoid(PneumaticsModuleType.REVPH, 0, 1);
  
  // Changed from SparkMax to Spark, using PWM ports 2 and 3
  private final Spark m_leftSparkPWM_additional = new Spark(2); // Was m_leftSparkMax
  private final Spark m_rightSparkPWM_additional = new Spark(3); // Was m_rightSparkMax

  private final Spark m_leftSparkPWM = new Spark(0);
  private final Spark m_rightSparkPWM = new Spark(1);
  private final WPI_TalonSRX m_armMotor = new WPI_TalonSRX(3);
  private final Encoder m_armEncoder = new Encoder(0, 1);
  private final MotorControllerGroup m_leftMotors;
  private final MotorControllerGroup m_rightMotors;
  private final DifferentialDrive m_robotDrive;


  // // Declare controller for pivot system
  private final static PivotSubsystem m_PivotSubsystem = new PivotSubsystem(); 
 
   /* Called once at the beginning of the robot program. */
  public Robot() {
    // Now grouping all Spark PWM motors
    m_leftMotors = new MotorControllerGroup(m_leftSparkPWM_additional, m_leftSparkPWM);
    m_rightMotors = new MotorControllerGroup(m_rightSparkPWM_additional, m_rightSparkPWM);

    m_robotDrive = new DifferentialDrive(m_leftMotors, m_rightMotors);

    SendableRegistry.addChild(m_robotDrive, m_leftMotors);
    SendableRegistry.addChild(m_robotDrive, m_rightMotors);
    // Register the newly changed Spark motors
    SendableRegistry.addChild(m_leftSparkPWM_additional, m_leftSparkPWM_additional);
    SendableRegistry.addChild(m_rightSparkPWM_additional, m_rightSparkPWM_additional);
    // Original Spark motors
    SendableRegistry.addChild(m_leftSparkPWM, m_leftSparkPWM);
    SendableRegistry.addChild(m_rightSparkPWM, m_rightSparkPWM);

    SendableRegistry.addChild(m_armMotor, m_armMotor);
    SendableRegistry.addChild(m_armEncoder, m_armEncoder);
  }

  @Override
  public void autonomousInit() {
    timer.reset();
    timer.start();
    m_doubleSolenoid.set(Value.kReverse);
    m_armEncoder.reset();
  }

  @Override
  public void autonomousPeriodic() {
    double time = timer.get();

    if (time < 2.0) {
      m_robotDrive.arcadeDrive(0.3, 0.0);
      m_doubleSolenoid.set(Value.kReverse);
      m_armMotor.set(0.1);
    } else if (time >= 2.0 && time < 3.0) {
      m_robotDrive.stopMotor();
      m_doubleSolenoid.set(Value.kForward);
      m_armMotor.set(0.0);
    } else {
      m_robotDrive.stopMotor();
      m_doubleSolenoid.set(Value.kReverse);
      m_armMotor.set(0.0);
    }
  }

  @Override
  public void teleopPeriodic() {
    controlSolenoid();

    // //call method to intake pivot position 
    controlPivot();

    // // Perform arcade drive using controller input: left stick X to turn,
    // // right trigger forward, left trigger reverse
    m_robotDrive.arcadeDrive(-m_controller.getLeftX(), 
                              m_controller.getRightTriggerAxis() - m_controller.getLeftTriggerAxis());
    m_robotDrive.arcadeDrive(-m_controller.getLeftX(),
                             m_controller.getRightTriggerAxis() - m_controller.getLeftTriggerAxis());

    m_armMotor.set(m_controller.getRightY());
  }

  private void controlSolenoid() {
    if (m_controller.getAButtonPressed()) {
      m_doubleSolenoid.set(Value.kForward);
    } else if (m_controller.getBButtonPressed()) {
      m_doubleSolenoid.set(Value.kReverse);
    } else {
      m_doubleSolenoid.set(Value.kOff);
    }
  }
  private void controlPivot() {
    if (m_controller.getXButtonPressed()) {
      m_PivotSubsystem.pivotPIDControl(0.3);
    }
  }
}

}
