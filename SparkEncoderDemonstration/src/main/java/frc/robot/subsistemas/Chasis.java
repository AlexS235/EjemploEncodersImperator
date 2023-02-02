// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsistemas;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Chasis extends SubsystemBase {
  CANSparkMax izq1, izq2;
  CANSparkMax der1, der2;

  MotorControllerGroup motoresIzq, motoresDer;

  double valorEncoderIzq, distanciaRecorridaMetros;

  double relacionEngranes = 8 / 3; // Motor / Llanta
  double diametroLlanta = 0.1016; // Metros

  /** Creates a new DriveTrain. */
  public Chasis() {
    izq1 = new CANSparkMax(1, MotorType.kBrushless);
    izq2 = new CANSparkMax(2, MotorType.kBrushless);
    der1 = new CANSparkMax(3, MotorType.kBrushless);
    der2 = new CANSparkMax(4, MotorType.kBrushless);

    izq1.setIdleMode(IdleMode.kBrake);
    izq2.setIdleMode(IdleMode.kBrake);
    der1.setIdleMode(IdleMode.kBrake);
    der2.setIdleMode(IdleMode.kBrake);

    motoresIzq = new MotorControllerGroup(izq1, izq2);
    motoresDer = new MotorControllerGroup(der1, der2);

    motoresDer.setInverted(true);
  }

  public void manejar(double velocidad, double giro) {
    double potenciaIzq = velocidad + giro;
    double potenciaDer = velocidad - giro;

    motoresIzq.set(potenciaIzq);
    motoresDer.set(potenciaDer);
  }

  public boolean manejarDistancia(double distanciaObjetivo, double potencia) {
    manejar(potencia, 0);

    if (distanciaRecorridaMetros >= distanciaObjetivo) {
      manejar(0, 0);
      return true;
    }

    return false;
  }

  @Override
  public void periodic() {
    valorEncoderIzq = izq1.getEncoder().getPosition();
    distanciaRecorridaMetros = valorEncoderIzq * (Math.PI * diametroLlanta / relacionEngranes);
  }
}
