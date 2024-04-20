import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IMedicalAppointments } from '../medical-appointments.model';

@Component({
  standalone: true,
  selector: 'jhi-medical-appointments-detail',
  templateUrl: './medical-appointments-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class MedicalAppointmentsDetailComponent {
  medicalAppointments = input<IMedicalAppointments | null>(null);

  previousState(): void {
    window.history.back();
  }
}
