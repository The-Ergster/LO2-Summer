package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.internal.opmode.TelemetryImpl;

@TeleOp
public class CopyOffTheNotebook extends OpMode {
    public CRServo CRServo;
    public TelemetryImpl telemetry;
    // Reminder to self that this may need to be used
    public double power = 0;
    public int i = 0;

    public void init() {
        CRServo = hardwareMap.get(CRServo.class, "servo");
        telemetry = new TelemetryImpl(this);
        // i=0;
        CRServo.setDirection(DcMotorSimple.Direction.FORWARD);
    }

// This code moves the
    public void loop() {
        if(i == 0){
            CRServo.setPower(0);
        }

        if(gamepad1.a && power <= 1 && power >= -1) {
            // Makes servo move
            power = 1;
            CRServo.setPower(power);
            // prints on phone
            //telemetry.addLine("Went to Power " + power);
            telemetry.addLine().addData("", "Went to power " + power);
            //telemetry.update();
        }
        if(gamepad1.b && power <= 1 && power >= -1) {
            // Makes servo move
            power = 0;
            CRServo.setPower(power);
            // prints on phone
//            telemetry.addLine("Went to Power " + power);
            telemetry.addLine().addData("", "Went to power " + power);
           // telemetry.update();
        }
        //telemetry.addLine("You out - power is now " + power);
        telemetry.update();
        i++;
    }
}