import { Component, signal } from '@angular/core';
import { Home } from './home/home';

@Component({
  selector: 'app-root',
  imports: [Home],
  templateUrl: './app.html',
})
export class App {
  protected readonly title = signal('frontend');
}
