import 'package:flutter/material.dart';

/// Top app bar for the map screen.
///
/// Displays the tour name and a back-navigation icon button.
class MapTopBar extends StatelessWidget implements PreferredSizeWidget {
  const MapTopBar({
    super.key,
    required this.tourName,
    required this.onBackClick,
  });

  final String tourName;
  final VoidCallback onBackClick;

  @override
  Size get preferredSize => const Size.fromHeight(kToolbarHeight);

  @override
  Widget build(BuildContext context) {
    final colorScheme = Theme.of(context).colorScheme;
    final textTheme = Theme.of(context).textTheme;

    return AppBar(
      backgroundColor: colorScheme.primary,
      foregroundColor: Colors.white,
      elevation: 0,
      leading: IconButton(
        icon: const Icon(Icons.arrow_back),
        tooltip: 'Back',
        onPressed: onBackClick,
      ),
      title: Text(
        tourName,
        style: textTheme.titleMedium?.copyWith(
          color: Colors.white,
          fontWeight: FontWeight.bold,
        ),
      ),
    );
  }
}
