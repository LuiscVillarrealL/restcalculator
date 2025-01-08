import { CommonModule } from '@angular/common';
import { Component, DestroyRef, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-calculator',
  templateUrl: './calculator.component.html',
  styleUrls: ['./calculator.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule],
})
export class CalculatorComponent implements OnInit {
  // Reactive signals for loading state and errors
  isFetching = signal(false);
  error = signal('');

  // Basic input fields
  number1: number | string = 0;
  number2: number | string = 0;
  operation = 'sum'; // Could be 'sum', 'sub', 'multi', 'div'
  result: number | string = '';

  private httpClient = inject(HttpClient);
  private destroyRef = inject(DestroyRef);

  ngOnInit() {
    // Optionally, if you still want a call on component load, do it here
    // this.fetchResult(); // or remove if you don't want an initial call
  }

  /**
   * Method triggered by a button click.
   * Builds the URL based on the chosen operation and user inputs.
   */
  onCalculate() {
    this.isFetching.set(true);
    this.error.set('');

    // Build the URL dynamically
    // e.g. http://localhost:8081/api/calculator/sum?num1=2&num2=1
    const apiUrl = `http://localhost:8081/api/calculator/${this.operation}?a=${this.number1}&b=${this.number2}`;

    const subscription = this.httpClient
      .get<{ result: string }>(apiUrl, {
        observe: 'response',
      })
      .subscribe({
        next: (response) => {
          this.result = response.body?.result ?? '';
          console.log(response);
          console.log(response.body?.result);
          this.isFetching.set(false);
        },
        error: (err) => {
          const errorMessage = err.error?.result || 'An unexpected error occurred';
          this.error.set(errorMessage);         
          this.result = 'Error';
          this.isFetching.set(false);
        },
        complete: () => {
          this.isFetching.set(false);
        },
      });

    this.destroyRef.onDestroy(() => {
      subscription.unsubscribe();
    });
  }
}
