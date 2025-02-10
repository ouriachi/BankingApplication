import { Component } from '@angular/core';

@Component({
  selector: 'app-home',
  standalone: true,
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  services = [
    { title: 'Comptes & Cartes', description: 'Ouvrez un compte et bénéficiez d’une carte bancaire adaptée à vos besoins.' },
    { title: 'Crédits', description: 'Financez vos projets grâce à nos offres de crédit attractives.' },
    { title: 'Investissements', description: 'Faites fructifier votre argent avec nos solutions d’investissement.' }
  ];
}
