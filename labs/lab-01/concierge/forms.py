# concierge/forms.py
from django import forms
from django.contrib.auth.forms import UserCreationForm
from .models import CustomUser, VisitorLog

class CustomUserCreationForm(UserCreationForm):
    class Meta(UserCreationForm.Meta):
        model = CustomUser
        fields = UserCreationForm.Meta.fields + ('role', 'first_name', 'last_name')


class VisitorLogForm(forms.ModelForm):
    class Meta:
        model = VisitorLog
        fields = ['visitor_name', 'action']
        widgets = {
            'visitor_name': forms.TextInput(attrs={'class': 'form-control', 'placeholder': 'Введіть ПІБ'}),
            'action': forms.Select(attrs={'class': 'form-control'}),
        }