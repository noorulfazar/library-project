import { CanActivateFn, Router } from '@angular/router';
export const authGuard: CanActivateFn = () => {
  const token = localStorage.getItem('token');
  if (!token) { new Router().navigate(['/login']); return false; }
  return true;
};
