import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ITypeDiseases } from '../type-diseases.model';

@Component({
  standalone: true,
  selector: 'jhi-type-diseases-detail',
  templateUrl: './type-diseases-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class TypeDiseasesDetailComponent {
  typeDiseases = input<ITypeDiseases | null>(null);

  previousState(): void {
    window.history.back();
  }
}
