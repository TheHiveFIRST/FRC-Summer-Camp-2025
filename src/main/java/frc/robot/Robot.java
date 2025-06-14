// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
package frc.robot;

import edu.wpi.first.util.sendable.SendableRegistry;
import com.revrobotics.spark.SparkLowLevel.MotorType;
// NEW IMPORTS FOR REVLIB 2025 - CORRECTED PATHS FOR ENUMS
import com.revrobotics.spark.SparkMax;
// Corrected import paths for IdleMode, ResetMode, PersistMode
// import com.revrobotics.spark.SparkBase.PersistMode; // This one still seems to be directly under SparkBase for now based on docs/examples
// import com.revrobotics.spark.SparkBase.ResetMode; // This one still seems to be directly under SparkBase for now based on docs/examples
// import com.revrobotics.spark.config.SparkBaseConfig.IdleMode; // Corrected path

import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends TimedRobot {
  private final Timer timer = new Timer();
  private final XboxController m_controller = new XboxController(0);

  private final DoubleSolenoid m_doubleSolenoid = new DoubleSolenoid(PneumaticsModuleType.REVPH, 0, 1);;
  
  private final SparkMax m_leftMotor1 = new SparkMax(1, MotorType.kBrushed);
  private final SparkMax m_leftMotor2 = new SparkMax(2, MotorType.kBrushed);
  private final SparkMax m_rightMotor3 = new SparkMax(3, MotorType.kBrushed);
  private final SparkMax m_rightMotor4 = new SparkMax(4, MotorType.kBrushed);

  private final SparkMaxConfig leftConfig = new SparkMaxConfig();
  private final SparkMaxConfig rightConfig = new SparkMaxConfig();

  private final DifferentialDrive m_robotDrive;

  /** Called once at the beginning of the robot program. */
  public Robot() {
    leftConfig.follow(2, false);
    rightConfig.follow(4, false);

    m_leftMotor1.configure(leftConfig, null, null);
    m_rightMotor3.configure(rightConfig, null, null);

    m_robotDrive = new DifferentialDrive(m_leftMotor2::set, m_rightMotor4::set);

    SendableRegistry.addChild(m_robotDrive, m_leftMotor2);
    SendableRegistry.addChild(m_robotDrive, m_rightMotor4);
  }

  @Override
  public void autonomousPeriodic() {
    if (timer.get() % 0.33 >= 0.16) {
      m_doubleSolenoid.set(Value.kForward);
    }
    else {
      m_doubleSolenoid.set(Value.kReverse);
    }
  }

  @Override
  public void teleopPeriodic() {
    // Steer with left stick, throttle with triggers.
    // Right trigger moves forward, left trigger moves backward.

    controlSolenoid();
    m_robotDrive.arcadeDrive(-m_controller.getLeftX(), m_controller.getRightTriggerAxis() - m_controller.getLeftTriggerAxis());
  }

  private void controlSolenoid() {
    if (m_controller.getAButtonPressed()) {
      m_doubleSolenoid.set(Value.kForward);
    } 
    else if (m_controller.getBButtonPressed()) {
      m_doubleSolenoid.set(Value.kReverse);
    }
    else {
      m_doubleSolenoid.set(Value.kOff);
    }
  }
}