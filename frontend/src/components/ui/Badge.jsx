// src/components/ui/Badge.jsx
import { getStatusClass, formatStatusLabel } from '../../utils/status';

export default function Badge({ status }) {
  if (!status) return null;
  const cls = getStatusClass(status);
  return (
    <span className={`bd ${cls}`}>
      {formatStatusLabel(status)}
    </span>
  );
}