import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, tap } from 'rxjs';
import { environment } from '../../environments/environment';

export interface AuthResponse { token:string; role:string; }
@Injectable({ providedIn: 'root' })
export class AuthService {
  private _token$ = new BehaviorSubject<string | null>(localStorage.getItem('token'));
  token$ = this._token$.asObservable();
  get token(){ return this._token$.value; }
  get isLoggedIn(){ return !!this.token; }
  get role(){ return localStorage.getItem('role') || ''; }

  constructor(private http: HttpClient) {}

  login(email:string, password:string){
    return this.http.post<AuthResponse>(`${environment.api}/api/auth/login`, {email,password})
      .pipe(tap(r => { localStorage.setItem('token', r.token); localStorage.setItem('role', r.role); this._token$.next(r.token); }));
  }
  logout(){ localStorage.clear(); this._token$.next(null); }
}
