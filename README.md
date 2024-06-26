
This is a demo of kubernetes autoscaling on custom prometeus metrics.
If autoscaling exeeds its maximum limit of 10 then a custom alert will be pushed to a telegram bot.

In alert-bot-secret.yaml change botToken.
In alertmanager-config-example.yaml change chatID.

On a Windows environment:
Execute apply.bat to apply kuber config.
Run Apache JMeter 5.5 with test.yaml to make some load.

Execute delete.bat to delete everything from kuber.

Based on example-voting-app from docker public repository.
