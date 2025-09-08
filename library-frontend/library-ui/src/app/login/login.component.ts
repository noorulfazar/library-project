// src/app/login/login.component.ts
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../auth/auth.service';
@Component({
  standalone: true, selector: 'app-login',
  templateUrl: './login.component.html', imports:[FormsModule]
})
export class LoginComponent {
  email='member@lib.com'; password='member123'; err='';
  constructor(private auth:AuthService, private router:Router){}
  submit(){
    this.auth.login(this.email, this.password).subscribe({
      // next: _ => this.router.navigateByUrl('/'),
      next: () => this.router.navigateByUrl('/books'),
      error: e => this.err = e.error?.message || 'Login failed'
    });
  }
}
