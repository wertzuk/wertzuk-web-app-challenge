import { Injectable } from '@angular/core';
import { Denomination } from './models';

@Injectable({
  providedIn: 'root',
})
export class CalculationService {
  async calculateInBackend(amount: number, previousAmount: number | null): Promise<Denomination[]> {
    let url = `http://localhost:8080/calculate?amount=${amount}`;
    if (previousAmount) {
      url += `&previousAmount=${previousAmount}`;
    }
    const response = await fetch(url);
    if (!response.ok) {
      throw new Error('Calculation failed');
    }
    return response.json();
  }

  calculateInFrontend(amount: number, previousAmount: number | null): Denomination[] {
    const denominations = this.doCalculation(this.toCents(amount));

    if (previousAmount !== null) {
      const oldDenominations = this.doCalculation(this.toCents(previousAmount));
      for (let i = 0; i < denominations.length; i++) {
        denominations[i].difference = denominations[i].count - oldDenominations[i].count;
      }
    }

    return denominations.map(d => ({ ...d, value: this.fromCents(d.value) }));
  }

  private doCalculation(amountInCents: number): Denomination[] {
    const denominations: Denomination[] = [
      { value: 20000, count: 0, difference: 0 },
      { value: 10000, count: 0, difference: 0 },
      { value: 5000, count: 0, difference: 0 },
      { value: 2000, count: 0, difference: 0 },
      { value: 1000, count: 0, difference: 0 },
      { value: 500, count: 0, difference: 0 },
      { value: 200, count: 0, difference: 0 },
      { value: 100, count: 0, difference: 0 },
      { value: 50, count: 0, difference: 0 },
      { value: 20, count: 0, difference: 0 },
      { value: 10, count: 0, difference: 0 },
      { value: 5, count: 0, difference: 0 },
      { value: 2, count: 0, difference: 0 },
      { value: 1, count: 0, difference: 0 },
    ];

    let remaining = amountInCents;

    for (const denom of denominations) {
      denom.count = Math.floor(remaining / denom.value);
      remaining = remaining % denom.value;
    }

    return denominations;
  }

  private toCents(amount: number): number {
    return Math.round(amount * 100);
  }

  private fromCents(cents: number): number {
    return cents / 100;
  }
}
