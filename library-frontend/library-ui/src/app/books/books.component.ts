// src/app/books/books.component.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { BooksService, Book } from './books.service';

// tiny helper to read email ("sub") from JWT
function emailFromToken(token: string | null): string {
  if (!token) return '';
  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.sub || '';
  } catch {
    return '';
  }
}

@Component({
  standalone: true,
  selector: 'app-books',
  imports: [CommonModule, FormsModule],
  templateUrl: './books.component.html',
})
export class BooksComponent {
  all: Book[] = [];
  q = '';
  msg = '';

  // 👇 add the view toggle here
  view: 'all' | 'available' | 'borrowed' = 'all';

  isAdmin = localStorage.getItem('role') === 'ROLE_ADMIN';
  currentEmail = emailFromToken(localStorage.getItem('token'));

  newBook = { title: '', author: '', isbn: '' };

  constructor(private api: BooksService, private router: Router) {
    this.reload(); // use the new reload on init
  }

  // 👇 load based on selected view
  reload() {
    if (this.view === 'available') {
      this.api.listAvailable().subscribe(b => (this.all = b));
    } else if (this.view === 'borrowed') {
      this.api.listBorrowed().subscribe(b => (this.all = b));
    } else {
      this.api.listAll().subscribe(b => (this.all = b));
    }
  }

  // (optional) keep a generic load if you like; not used now
  // load() { this.api.listAll().subscribe(b => (this.all = b)); }

  filtered() {
    const needle = this.q.toLowerCase();
    return this.all.filter(
      b => b.title.toLowerCase().includes(needle) || b.author.toLowerCase().includes(needle)
    );
  }

  borrow(id: number) {
    this.api.borrow(id).subscribe({
      next: _ => { this.msg = 'Borrowed!'; this.reload(); },
      error: e => { this.msg = e?.error?.message || 'Borrow failed'; }
    });
  }

  giveBack(id: number) {
    this.api.giveBack(id).subscribe({
      next: _ => { this.msg = 'Returned!'; this.reload(); },
      error: e => { this.msg = e?.error?.message || 'Return not allowed'; }
    });
  }

  add() {
    if (!this.isAdmin) return;
    this.api.add(this.newBook).subscribe({
      next: _ => {
        this.newBook = { title: '', author: '', isbn: '' };
        this.reload();
      },
      error: e => { this.msg = e?.error?.message || 'Add failed'; }
    });
  }

  logout() {
    localStorage.clear();
    this.router.navigateByUrl('/login');
  }
}
