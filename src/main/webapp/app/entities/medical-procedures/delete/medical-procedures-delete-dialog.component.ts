import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMedicalProcedures } from '../medical-procedures.model';
import { MedicalProceduresService } from '../service/medical-procedures.service';

@Component({
  standalone: true,
  templateUrl: './medical-procedures-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MedicalProceduresDeleteDialogComponent {
  medicalProcedures?: IMedicalProcedures;

  protected medicalProceduresService = inject(MedicalProceduresService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.medicalProceduresService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
