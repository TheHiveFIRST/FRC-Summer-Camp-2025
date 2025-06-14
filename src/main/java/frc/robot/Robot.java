// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.XboxController;

// NEW IMPORTS FOR REVLIB 2025 - CORRECTED PATHS FOR ENUMS
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;

// Corrected import paths for IdleMode, ResetMode, PersistMode
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode; // Corrected path
import com.revrobotics.spark.SparkBase.ResetMode; // This one still seems to be directly under SparkBase for now based on docs/examples
import com.revrobotics.spark.SparkBase.PersistMode; // This one still seems to be directly under SparkBase for now based on docs/examples


/**
 * This is a demo program showing the use of the DifferentialDrive class, specifically it contains
 * the code necessary to operate a robot with tank drive.
 */
public class Robot extends TimedRobot {
  private final DifferentialDrive m_robotDrive;
  private final XboxController m_controller;

  // Change to SparkMax (no CAN prefix)
  private final SparkMax m_leftLeader = new SparkMax(1, MotorType.kBrushed);
  private final SparkMax m_leftFollower = new SparkMax(2, MotorType.kBrushed);
  private final SparkMax m_rightLeader = new SparkMax(3, MotorType.kBrushed);
  private final SparkMax m_rightFollower = new SparkMax(4, MotorType.kBrushed);

  /** Called once at the beginning of the robot program. */
  public Robot() {
    // The DifferentialDrive class expects individual motor controllers.
    // We pass the leader motors to DifferentialDrive, and the followers will
    // automatically match their leader's output due to the .follow() configuration.
    m_robotDrive = new DifferentialDrive(m_leftLeader, m_rightLeader);

    // Optional: Set a safety expiration for the motors. If motor updates stop,
    // motors will be disabled after this time (in seconds).
    m_robotDrive.setSafetyEnabled(true);
    m_robotDrive.setExpiration(0.1); // Default is 0.1 seconds, can be adjusted

    m_controller = new XboxController(0);
  }

  @Override
  public void teleopPeriodic() {
    // Drive the robot using tank drive with Xbox controller Y-axes
    m_robotDrive.tankDrive(m_controller.getLeftY(), m_controller.getRightY());
  }

  @Override
  public void robotInit() {
    // --- NEW REVLIB 2025 CONFIGURATION SYSTEM ---

    // Create configuration objects
    SparkMaxConfig leftLeaderConfig = new SparkMaxConfig();
    SparkMaxConfig leftFollowerConfig = new SparkMaxConfig();
    SparkMaxConfig rightLeaderConfig = new SparkMaxConfig();
    SparkMaxConfig rightFollowerConfig = new SparkMaxConfig();

    // Configure follower motors
    leftFollowerConfig.follow(m_leftLeader.getDeviceId(), false); // false for not inverted
    rightFollowerConfig.follow(m_rightLeader.getDeviceId(), true); // true for inverted, as right side needs inversion

    // Invert the right side leader motor
    rightLeaderConfig.inverted(true); // Invert the leader

    // Example of setting IdleMode (e.g., to Coast)
    leftLeaderConfig.idleMode(IdleMode.kCoast);
    leftFollowerConfig.idleMode(IdleMode.kCoast);
    rightLeaderConfig.idleMode(IdleMode.kCoast);
    rightFollowerConfig.idleMode(IdleMode.kCoast);

    // Apply configurations to the motors
    m_leftLeader.configure(leftLeaderConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    m_leftFollower.configure(leftFollowerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    m_rightLeader.configure(rightLeaderConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    m_rightFollower.configure(rightFollowerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }
}