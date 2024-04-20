import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IMedicalProcedures } from '../medical-procedures.model';

@Component({
  standalone: true,
  selector: 'jhi-medical-procedures-detail',
  templateUrl: './medical-procedures-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class MedicalProceduresDetailComponent {
  medicalProcedures = input<IMedicalProcedures | null>(null);

  previousState(): void {
    window.history.back();
  }
}
