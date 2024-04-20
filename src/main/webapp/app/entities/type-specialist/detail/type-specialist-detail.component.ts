import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ITypeSpecialist } from '../type-specialist.model';

@Component({
  standalone: true,
  selector: 'jhi-type-specialist-detail',
  templateUrl: './type-specialist-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class TypeSpecialistDetailComponent {
  typeSpecialist = input<ITypeSpecialist | null>(null);

  previousState(): void {
    window.history.back();
  }
}
