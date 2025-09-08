// src/app/books/books.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { map } from 'rxjs/operators';

export interface Book {
  id: number;
  title: string;
  author: string;
  isbn: string;
  available: boolean;
  borrowedByEmail?: string; // backend should include this in DTO
}

@Injectable({ providedIn: 'root' })
export class BooksService {
  constructor(private http: HttpClient) {}

  // core list with optional availability filter
  list(available?: boolean | null) {
    const base = `${environment.api}/api/books`;
    const url =
      available === null || available === undefined ? base : `${base}?available=${available}`;
    return this.http.get<Book[]>(url);
  }

  listAll()       { return this.list(null); }
  listAvailable() { return this.list(true); }
  listBorrowed()  { return this.list(false); }

  // optional front-end search helper (not required)
  search(q: string) {
    const needle = q.trim().toLowerCase();
    return this.listAll().pipe(
      map(books =>
        books.filter(
          b => b.title.toLowerCase().includes(needle) || b.author.toLowerCase().includes(needle)
        )
      )
    );
  }

  borrow(id: number)   { return this.http.post<Book>(`${environment.api}/api/books/${id}/borrow`, {}); }
  giveBack(id: number) { return this.http.post<Book>(`${environment.api}/api/books/${id}/return`, {}); }
  add(b: { title: string; author: string; isbn: string }) {
    return this.http.post<Book>(`${environment.api}/api/books`, b);
  }
}
