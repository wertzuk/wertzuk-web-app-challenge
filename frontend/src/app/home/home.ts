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
  isUsingFrontend = signal(false);
  isFirstCalculation = signal(true);

  displayedCurrentAmount: number | null = null;
  displayedPreviousAmount: number | null = null;

  denominations = signal<Denomination[]>([]);

  async calculate() {
    console.log(this.service)
    if (this.isLoading()) return;

    if (this.amount === this.previousAmount) {
      alert('Selber Betrag wie in der vorherigen Berechnung. Bitte ändern Sie den Betrag, um erneut zu berechnen.');
      return;
    }

    this.isLoading.set(true);

    if (this.previousAmount) {
      this.isFirstCalculation.set(false);
    }


    try {
      const result = await this.service.calculateInBackend(this.amount!, this.previousAmount) as Denomination[];
      this.denominations.set(result);
      this.isCompleted.set(true);
      this.displayedPreviousAmount = this.previousAmount;
      this.displayedCurrentAmount = this.amount;
      this.previousAmount = this.amount;
      this.amount = null;
    } catch (error) {
      alert('Serverfehler.Betrag konnte nicht berechnet werden.');
      return;
    } finally {
      this.isLoading.set(false);
    }

  }
}
