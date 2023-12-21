package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

  @TeleOp
  public class LastYearsCodeButAgain extends OpMode {
      private CRServo clawServo;
      private long lastPressed;


      @Override
      public void init() {
          clawServo = hardwareMap.get(CRServo.class, "cs");
          lastPressed = System.currentTimeMillis();
          clawServo.setDirection(DcMotorSimple.Direction.FORWARD);
      }

      /*    public void clawControl(boolean clawOpen, boolean clawClose) {
              clawClose = false;
              double clawPower;
              if (clawClose == true) {
                  clawPower = 1;
              } else if (clawOpen == true) {
                  clawPower = -1;
              } else {
                  clawPower = 0;
              }

              clawServo.setPower(clawPower);
          } */

      @Override
      public void loop() {
          //clawControl(gamepad1.a, gamepad1.b);

  /*        //if (System.currentTimeMillis() - this.lastPressed > 500) {
              if (this.gamepad1.a) {
                  // Open the grabber up
                  // Turn the power back on (see below if case)
                  this.clawServo.getController().pwmEnable();
                  this.clawServo.setPower(1.0);

                  // Reset the pressed time
                  this.lastPressed = System.currentTimeMillis();
              } else if (this.gamepad1.b) {
                  // Close it up
                  this.clawServo.setPower(-1.0);

                  // Turn the power of the grabber OFF.
                  // This allows the servo to claw to fully retract.
                  this.clawServo.getController().pwmDisable();

                  // Reset the pressed time
                  this.lastPressed = System.currentTimeMillis();
              }
          }
      //} */

          //Another attempt
          if (System.currentTimeMillis() - this.lastPressed > 500) {
              if (this.gamepad1.a) {
                  // Open the grabber up
                  // Turn the power back on (see below if case)
                  this.clawServo.getController().pwmEnable();
                  this.clawServo.setPower(1);

                  // Reset the pressed time
                  this.lastPressed = System.currentTimeMillis();
              } else if (this.gamepad1.b) {
                  // Close it up
                  this.clawServo.setPower(-1);

                  // Turn the power of the grabber OFF.
                  // This allows the servo to claw to fully retract.
                  this.clawServo.getController().pwmDisable();

                  // Reset the pressed time
                  this.lastPressed = System.currentTimeMillis();
              }
          }
      }
  }
