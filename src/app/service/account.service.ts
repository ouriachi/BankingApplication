import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';


export interface Account {
  accountNumber: string;
  accountHolder: string;
  balance: number;
}

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  private apiUrl = '/api/accounts'; // correspond au proxy configuré.
  private apiCreateAccount = '/api/createAccount'

  

  constructor(private httpClient : HttpClient) { }


  getAccount(): Observable<Account[]> {

    return this.httpClient.get<Account[]>(this.apiUrl)


  }

  createAccount(account : Account) : Observable<Account> {

    return this.httpClient.post<Account>(this.apiCreateAccount,account);
  }
    
}
