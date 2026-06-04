# concierge/models.py
from django.db import models
from django.contrib.auth.models import AbstractUser

class CustomUser(AbstractUser):
    ROLE_CHOICES = (
        ('concierge', 'Консьєрж'),
        ('admin', 'Адміністратор'),
    )
    role = models.CharField(max_length=20, choices=ROLE_CHOICES, default='concierge')

    def __str__(self):
        return f"{self.username} ({self.get_role_display()})"


class VisitorLog(models.Model):
    ACTION_CHOICES = (
        ('in', 'Увійшов'),
        ('out', 'Вийшов'),
    )
    
    visitor_name = models.CharField(max_length=100, verbose_name="ПІБ відвідувача")
    action = models.CharField(max_length=3, choices=ACTION_CHOICES, verbose_name="Дія")
    timestamp = models.DateTimeField(auto_now_add=True, verbose_name="Час події")
    recorded_by = models.ForeignKey(CustomUser, on_delete=models.SET_NULL, null=True, verbose_name="Записав консьєрж")

    class Meta:
        ordering = ['-timestamp']  

    def __str__(self):
        return f"{self.visitor_name} - {self.get_action_display()} ({self.timestamp:%H:%M:%S})"