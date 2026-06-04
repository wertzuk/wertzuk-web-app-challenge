import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Denomination } from '../models';
import { CalculationService } from '../calculation-service';

@Component({
  selector: 'app-home',
  imports: [FormsModule],
  templateUrl: './home.html',
})
export class Home {
  private service = inject(CalculationService);

  amount: number | null = null;

  previousAmount: number | null = null;
  isLoading = signal(false);
  isCompleted = signal(false);
  isUsingFrontend = signal(true);
  isFirstCalculation = signal(true);

  displayedCurrentAmount: number | null = null;
  displayedPreviousAmount: number | null = null;

  denominations = signal<Denomination[]>([]);

  async calculate() {
    if (this.isLoading()) return;

    if (this.amount === this.previousAmount) {
      alert('Selber Betrag wie in der vorherigen Berechnung. Bitte ändern Sie den Betrag, um erneut zu berechnen.');
      return;
    }

    this.isCompleted.set(false);
    this.isLoading.set(true);

    if (this.previousAmount) {
      this.isFirstCalculation.set(false);
    }

    if (this.isUsingFrontend()) {
      console.log('Calculating in frontend');
      const result = this.service.calculateInFrontend(this.amount!, this.previousAmount);
      this.onCalculationSuccess(result);
      this.isLoading.set(false);
    } else {
      console.log('Calculating in backend');
      try {
        const result = await this.service.calculateInBackend(this.amount!, this.previousAmount) as Denomination[];
        this.onCalculationSuccess(result);
      } catch (error) {
        alert('Serverfehler.Betrag konnte nicht berechnet werden.');
        return;
      } finally {
        this.isLoading.set(false);
      }

    }
  }

  formatValue(value: number): string {
    return value < 1 ? `${Math.round(value * 100)}c` : `${value}€`;
  }

  private onCalculationSuccess(result: Denomination[]) {
    this.denominations.set(result);
    this.isCompleted.set(true);
    this.displayedPreviousAmount = this.previousAmount;
    this.displayedCurrentAmount = this.amount;
    this.previousAmount = this.amount;
    this.amount = null;
  }
}
