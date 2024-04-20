import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMedicalAppointments } from '../medical-appointments.model';
import { MedicalAppointmentsService } from '../service/medical-appointments.service';

@Component({
  standalone: true,
  templateUrl: './medical-appointments-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MedicalAppointmentsDeleteDialogComponent {
  medicalAppointments?: IMedicalAppointments;

  protected medicalAppointmentsService = inject(MedicalAppointmentsService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.medicalAppointmentsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
