import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IClinicHistory } from '../clinic-history.model';
import { ClinicHistoryService } from '../service/clinic-history.service';

@Component({
  standalone: true,
  templateUrl: './clinic-history-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ClinicHistoryDeleteDialogComponent {
  clinicHistory?: IClinicHistory;

  protected clinicHistoryService = inject(ClinicHistoryService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.clinicHistoryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
