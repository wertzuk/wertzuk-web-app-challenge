import { TestBed } from '@angular/core/testing';

import { CalculationService } from './calculation-service';

describe('CalculationService', () => {
  let service: CalculationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CalculationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('calculateInFrontend', () => {
    it('should return correct denominations for a simple amount', () => {
      const result = service.calculateInFrontend(50, null);
      const fifty = result.find(d => d.value === 50);
      expect(fifty?.count).toBe(1);
    });

    it('should return correct denominations for a complex amount', () => {
      const result = service.calculateInFrontend(48.57, null);
      expect(result.find(d => d.value === 20)?.count).toBe(2);
      expect(result.find(d => d.value === 5)?.count).toBe(1);
      expect(result.find(d => d.value === 2)?.count).toBe(1);
      expect(result.find(d => d.value === 1)?.count).toBe(1);
      expect(result.find(d => d.value === 0.5)?.count).toBe(1);
      expect(result.find(d => d.value === 0.05)?.count).toBe(1);
      expect(result.find(d => d.value === 0.02)?.count).toBe(1);
    });

    it('should calculate differences when previousAmount is provided', () => {
      const result = service.calculateInFrontend(50, 20);
      const twenty = result.find(d => d.value === 20);
      expect(twenty?.difference).toBe(-1);
    });

    it('should handle decimal amounts correctly', () => {
      const result = service.calculateInFrontend(0.03, null);
      expect(result.find(d => d.value === 0.02)?.count).toBe(1);
      expect(result.find(d => d.value === 0.01)?.count).toBe(1);
    });
  });


});
