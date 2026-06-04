# concierge/views.py
from django.shortcuts import render, redirect
from django.contrib.auth import login, logout
from django.contrib.auth.forms import AuthenticationForm
from django.contrib.auth.decorators import login_required
from .forms import CustomUserCreationForm, VisitorLogForm
from .models import VisitorLog

# concierge/views.py

def register_view(request):
    if request.method == 'POST':

        form = CustomUserCreationForm(request.POST)
        if form.is_valid():
            user = form.save()
            login(request, user)
            return redirect('dashboard')
    else:
        form = CustomUserCreationForm()

    return render(request, 'concierge/register.html', {'form': form})

def login_view(request):
    if request.method == 'POST':
        form = AuthenticationForm(data=request.POST)

        if form.is_valid():
            user = form.get_user()
            login(request, user)

            return redirect('dashboard')
    else:
        form = AuthenticationForm()

    return render(request, 'concierge/login.html', {'form': form})

def logout_view(request):
    logout(request)
    return redirect('login')

@login_required(login_url='login')
def dashboard_view(request):
    logs = VisitorLog.objects.all()

    if request.method == 'POST':
        form = VisitorLogForm(request.POST)
        if form.is_valid():
            visitor_log = form.save(commit=False)
            visitor_log.recorded_by = request.user  
            visitor_log.save()

            return redirect('dashboard')
    else:
        form = VisitorLogForm()
    
    return render(request, 'concierge/dashboard.html', {'logs': logs, 'form': form})