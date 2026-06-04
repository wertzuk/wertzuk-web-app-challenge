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

    return [];
  }
}
