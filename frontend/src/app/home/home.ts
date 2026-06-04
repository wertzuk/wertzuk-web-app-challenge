import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Denomination } from '../models';

@Component({
  selector: 'app-home',
  imports: [FormsModule],
  templateUrl: './home.html',
})
export class Home {
  amount: number | null = null;

  previousAmount = signal<number | null>(null);
  isLoading = signal(false);
  isCompleted = signal(false);
  isUsingFrontend = signal(false);
  isFirstCalculation = signal(true);

  denominations = signal<Denomination[]>([]);

  async calculate() {
    console.log('Calculate Denomatinations for: ', this.amount);
    if (this.previousAmount()) {
      this.isFirstCalculation.set(false);
    }

    this.isLoading.set(true);
    let url = `http://localhost:8080/calculate?amount=${this.amount}`;
    if (this.previousAmount()) {
      url += `&previousAmount=${this.previousAmount()}`;
    }

    console.log('Call Endpoint', url);

    const response = await fetch(url);
    this.isLoading.set(false);
    this.isCompleted.set(true);
    if (!response.ok) {
      alert('Error calculating amount');
      return;
    }

    this.previousAmount.set(this.amount);
    this.denominations.set((await response.json()) as Denomination[]);
  }
}
